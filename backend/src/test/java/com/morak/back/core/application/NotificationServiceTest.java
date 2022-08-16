package com.morak.back.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.Menu;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.SchedulingException;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class NotificationServiceTest {

    private final FakeApiReceiver receiver;

    private final SlackClient slackClient;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    private final NotificationService notificationService;

    private Team team;
    private Member member;

    @Autowired
    public NotificationServiceTest(TeamRepository teamRepository,
                                   TeamMemberRepository teamMemberRepository,
                                   SlackWebhookRepository slackWebhookRepository) {
        this.receiver = new FakeApiReceiver();
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.slackWebhookRepository = slackWebhookRepository;
        this.slackClient = new FakeSlackClient(receiver);
        this.notificationService =
                new NotificationService(slackClient, teamRepository, teamMemberRepository, slackWebhookRepository);
    }

    @BeforeEach
    void setUp() {
        team = teamRepository.findByCode("MoraK123").orElseThrow();
        member = teamMemberRepository.findAllByTeamId(team.getId()).get(0).getMember();
    }

    @Test
    void 웹훅_URL을_등록한다() {
        // given
        String url = "https://hooks.slack.com/services/my-url";
        SlackWebhookCreateRequest request = new SlackWebhookCreateRequest(url);

        // when
        Long webhookId = notificationService.saveSlackWebhook(team.getCode(), member.getId(), request);

        // then
        assertThat(webhookId).isNotNull();
    }

    /*
    data.sql 로 인해 위 등록테스트와 거의 같습니다. 이 부분은 논의 후 수정이 필요합니다.
     */
    @Test
    void 등록된_웹훅_URL을_변경하면_ID가_바뀐다() {
        // given
        String url = "https://hooks.slack.com/services/my-url";

        SlackWebhook savedWebhook = slackWebhookRepository.findByTeamId(team.getId()).orElseThrow();
        SlackWebhookCreateRequest request = new SlackWebhookCreateRequest(url);

        // when
        Long webhookId = notificationService.saveSlackWebhook(team.getCode(), member.getId(), request);

        // then
        assertThat(webhookId).isNotEqualTo(savedWebhook.getId());
    }

    @Test
    void 다른_팀의_웹훅을_등록할_수_없다() {
        // given
        String url = "https://hello.world";
        SlackWebhookCreateRequest request = new SlackWebhookCreateRequest(url);

        // when
        Member otherMember = teamMemberRepository.findAllByTeamId(
                teamRepository.findByCode("Betrayed").orElseThrow().getId()).get(0
        ).getMember();

        // then
        assertThatThrownBy(
                () -> notificationService.saveSlackWebhook(team.getCode(), otherMember.getId(), request)
        ).isInstanceOf(TeamAuthorizationException.class);
    }

    @Test
    void 상태가_OPEN이고_마감시간이_지난_투표를_마감하고_알림을_보낸다(@Autowired PollRepository pollRepository) {
        // given
        SlackWebhook slackWebhook = slackWebhookRepository.findByTeamId(team.getId()).orElseThrow();
        List<Poll> polls = pollRepository.findAllByTeamId(team.getId());
        Map<Menu, Optional<SlackWebhook>> menuWebhooks = toMenuWebhooks(polls, slackWebhook);// from data.sql

        // when
        notificationService.closeAndNotifyMenusByScheduled(menuWebhooks);

        // then
        Assertions.assertAll(
                () -> assertThat(receiver.getMessage()).contains(team.getName()),
                () -> assertThat(polls.get(0).getStatus().isClosed()).isTrue()
        );
    }

    @Test
    void 여러개의_알림을_보내다가_중간에_실패해도_알림은_계속된다(@Autowired PollRepository pollRepository) {
        // given
        SlackWebhook slackWebhook = slackWebhookRepository.findByTeamId(team.getId()).orElseThrow();

        Poll pollA = pollRepository.findByCode("testcode").orElseThrow();
        Poll pollB = pollRepository.save(
                Poll.builder()
                        .team(team)
                        .host(member)
                        .title("invalid")
                        .allowedPollCount(2)
                        .isAnonymous(false)
                        .status(PollStatus.OPEN)
                        .code(Code.generate(length -> "AAAA1111"))
                        .closedAt(LocalDateTime.now().plusMinutes(10))
                        .build()
        );
        Poll pollC = pollRepository.save(
                Poll.builder()
                        .team(team)
                        .host(member)
                        .title("title-2")
                        .allowedPollCount(2)
                        .isAnonymous(false)
                        .status(PollStatus.OPEN)
                        .code(Code.generate(length -> "BBBB2222"))
                        .closedAt(LocalDateTime.now().plusMinutes(10))
                        .build()
        );

        // when
        Map<Menu, Optional<SlackWebhook>> menuWebhooks =
                toMenuWebhooks(pollRepository.findAllByTeamId(team.getId()), slackWebhook);

        // then
        Assertions.assertAll(
                () -> assertThatThrownBy(
                        () -> notificationService.closeAndNotifyMenusByScheduled(menuWebhooks))
                        .isInstanceOf(SchedulingException.class)
                        .extracting("exceptions")
                        .asList()
                        .hasSize(1),
                () -> assertThat(pollA.getStatus().isClosed()).isTrue(),
                () -> assertThat(pollB.getStatus().isClosed()).isFalse(),
                () -> assertThat(pollC.getStatus().isClosed()).isTrue()
        );
    }

    private Map<Menu, Optional<SlackWebhook>> toMenuWebhooks(List<Poll> menus, SlackWebhook slackWebhook) {
        return menus.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        menu -> Optional.of(slackWebhook)
                ));
    }
}
