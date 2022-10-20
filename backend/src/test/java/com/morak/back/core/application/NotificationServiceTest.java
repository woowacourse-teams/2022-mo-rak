package com.morak.back.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.FormattableData;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.ui.dto.SlackWebhookCreateRequest;
import com.morak.back.core.util.MessageFormatter;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class NotificationServiceTest {

    private final FakeApiReceiver receiver;

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;

    private final NotificationService notificationService;

    private Team team;
    private Member member;

    @Autowired
    public NotificationServiceTest(TeamRepository teamRepository,
                                   TeamMemberRepository teamMemberRepository,
                                   SlackWebhookRepository slackWebhookRepository,
                                   MemberRepository memberRepository) {
        this.receiver = new FakeApiReceiver();
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.slackWebhookRepository = slackWebhookRepository;
        SlackClient slackClient = new FakeSlackClient(receiver);
        this.notificationService =
                new NotificationService(slackClient, teamRepository, teamMemberRepository,
                        slackWebhookRepository, memberRepository);
    }

    @BeforeEach
    void setUp() {
        team = teamRepository.findByCode("MoraK123").orElseThrow();
        member = teamMemberRepository.findAllByTeam(team).get(0).getMember();
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

        SlackWebhook savedWebhook = slackWebhookRepository.findByTeam(team).orElseThrow();
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
        Member otherMember = teamMemberRepository.findAllByTeam(
                teamRepository.findByCode("Betrayed").orElseThrow()
        ).get(0).getMember();

        // then
        assertThatThrownBy(
                () -> notificationService.saveSlackWebhook(team.getCode(), otherMember.getId(), request)
        ).isInstanceOf(TeamAuthorizationException.class);
    }

    @Test
    void 알림을_전송한다(@Autowired PollRepository pollRepository) {
        // given
        Poll poll = pollRepository.findByCode("testcode").orElseThrow();

        // when
        notificationService.notifyMenuStatus(team, MessageFormatter.formatClosed(FormattableData.from(poll)));

        // then
        String message = receiver.getMessage();
        assertThat(message).contains("마감되었습니다");
        System.out.println("message = " + message); // remained to debug
    }
}
