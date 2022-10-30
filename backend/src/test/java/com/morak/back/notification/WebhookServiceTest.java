package com.morak.back.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.notification.application.WebhookService;
import com.morak.back.notification.application.dto.SlackWebhookCreateRequest;
import com.morak.back.notification.domain.slack.FakeApiReceiver;
import com.morak.back.notification.domain.slack.SlackWebhook;
import com.morak.back.notification.domain.slack.SlackWebhookRepository;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class WebhookServiceTest {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SlackWebhookRepository slackWebhookRepository;
    private final WebhookService webhookService;

    @Autowired
    public WebhookServiceTest(
            FakeApiReceiver receiver,
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            SlackWebhookRepository slackWebhookRepository,
            WebhookService webhookService
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.slackWebhookRepository = slackWebhookRepository;
        this.webhookService = webhookService;
    }

    private Team team;
    private Member member;

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
        Long webhookId = webhookService.saveSlackWebhook(team.getCode(), member.getId(), request);

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
        Long webhookId = webhookService.saveSlackWebhook(team.getCode(), member.getId(), request);

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
                () -> webhookService.saveSlackWebhook(team.getCode(), otherMember.getId(), request)
        ).isInstanceOf(TeamAuthorizationException.class);
    }

    // todo : review this
//    @Test
//    @Disabled
//    void 알림을_전송한다(@Autowired PollRepository pollRepository) {
//        // given
//        Poll poll = pollRepository.findByCode("testcode").orElseThrow();
//
//        // when
//        webhookService.notifyMenuStatus(team, MessageFormatter.formatClosed(FormattableData.from(poll)));
//
//        // then
//        String message = receiver.getMessage();
//        assertThat(message).contains("마감되었습니다");
//        System.out.println("message = " + message); // remained to debug
//    }
}
