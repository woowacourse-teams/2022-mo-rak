package com.morak.back.poll.application;

import static com.morak.back.poll.domain.PollStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.poll.exception.PollNotFoundException;
import com.morak.back.poll.ui.dto.MemberResultResponse;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:schema.sql")
class PollServiceTest {

    private MemberRepository memberRepository;
    private TeamRepository teamRepository;
    private TeamMemberRepository teamMemberRepository;
    private PollRepository pollRepository;
    private PollItemRepository pollItemRepository;
    private SlackWebhookRepository slackWebhookRepository;
    private SlackClient slackClient;


    private NotificationService notificationService;
    private PollService pollService;

    private Member member;
    private Team team;
    private Poll poll;

    @Autowired
    public PollServiceTest(MemberRepository memberRepository, TeamRepository teamRepository,
                           TeamMemberRepository teamMemberRepository, PollRepository pollRepository,
                           PollItemRepository pollItemRepository, SlackWebhookRepository slackWebhookRepository) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.pollRepository = pollRepository;
        this.pollItemRepository = pollItemRepository;
        this.slackWebhookRepository = slackWebhookRepository;
        this.slackClient = new FakeSlackClient();
        this.notificationService = new NotificationService(slackClient, teamRepository,
                teamMemberRepository, slackWebhookRepository);
        this.pollService = new PollService(
                slackClient,
                pollRepository,
                memberRepository,
                teamRepository,
                teamMemberRepository,
                pollItemRepository,
                slackWebhookRepository,
                notificationService
        );
    }

    @BeforeEach
    void setup() {
        member = memberRepository.save(Member.builder()
                .oauthId("oauthmem")
                .name("박성우")
                .profileUrl("http://park-profile.com")
                .build());

        team = teamRepository.save(Team.builder()
                .name("team")
                .code(Code.generate(length -> "abcd1234"))
                .build());

        poll = pollRepository.save(Poll.builder()
                .team(team)
                .host(member)
                .title("test-tile")
                .allowedPollCount(3)
                .isAnonymous(false)
                .status(OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code(Code.generate(length -> "ABCD1234"))
                .build());

        teamMemberRepository.save(new TeamMember(null, team, member));
    }

    @Test
    void 투표를_생성한다() {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest(
                "title",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        // when
        String pollCode = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);

        // then
        assertThat(pollCode).hasSize(8);
    }

    @Test
    void 투표를_생성_시_멤버가_팀에_속해있지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());

        PollCreateRequest pollCreateRequest = new PollCreateRequest(
                "title",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        // when & then
        assertThatThrownBy(() -> pollService.createPoll(team.getCode(), 차리.getId(), pollCreateRequest))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 투표를_생성_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";
        PollCreateRequest pollCreateRequest = new PollCreateRequest(
                "title",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        // when & then
        assertThatThrownBy(() -> pollService.createPoll(invalidTeamCode, member.getId(), pollCreateRequest))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest(
                "title",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        String pollCode = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);

        // when
        List<PollResponse> polls = pollService.findPolls(team.getCode(), member.getId());

        // then
        assertThat(polls)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(
                        List.of(new PollResponse(poll.getId(), poll.getTitle(), poll.getAllowedPollCount(),
                                        poll.getIsAnonymous(),
                                        poll.getStatus().name(), poll.getCreatedAt(),
                                        poll.getClosedAt(), poll.getCode(), true),
                                new PollResponse(null, pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getIsAnonymous(), OPEN.name(), null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        true))
                );
    }

    @Test
    void 투표의_주인이_아닌_유저가_투표_목록을_조회할_수_있다() {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest(
                "title",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );
        String pollCode = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);

        Member 차리 = memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());
        teamMemberRepository.save(new TeamMember(null, team, 차리));

        // when
        List<PollResponse> polls = pollService.findPolls(team.getCode(), 차리.getId());

        // then
        assertThat(polls)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(
                        List.of(new PollResponse(poll.getId(), poll.getTitle(), poll.getAllowedPollCount(),
                                        poll.getIsAnonymous(),
                                        poll.getStatus().name(), poll.getCreatedAt(),
                                        poll.getClosedAt(), poll.getCode(), false),
                                new PollResponse(null, pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getIsAnonymous(), OPEN.name(), null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        false))
                );
    }

    @Test
    void 투표_목록_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.findPolls(invalidTeamCode, member.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표를_진행한다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub2")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        PollItem pollItem2 = pollItems.get(1);

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        // when
        pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"),
                        new PollResultRequest(pollItem2.getId(), "ㅋ")));

        // then
        Assertions.assertAll(
                () -> assertThat(pollItem1.getPollResults().get(0))
                        .extracting("pollItem", "member")
                        .containsExactly(pollItem1, member),
                () -> assertThat(pollItem2.getPollResults().get(0))
                        .extracting("pollItem", "member")
                        .containsExactly(pollItem2, member)
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub2")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub3")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        PollItem pollItem2 = pollItems.get(1);
        PollItem pollItem3 = pollItems.get(2);

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);
        poll.addItem(pollItem3);

        // when
        pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"),
                        new PollResultRequest(pollItem2.getId(), "ㅋ"),
                        new PollResultRequest(pollItem3.getId(), "ㅋ")));

        pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"),
                        new PollResultRequest(pollItem2.getId(), "ㅋ")));

        // then
        Assertions.assertAll(
                () -> assertThat(pollItem1.getPollResults().get(0))
                        .extracting("pollItem", "member")
                        .containsExactly(pollItem1, member),
                () -> assertThat(pollItem2.getPollResults().get(0))
                        .extracting("pollItem", "member")
                        .containsExactly(pollItem2, member),
                () -> assertThat(pollItem3.getPollResults()).hasSize(0)
        );
    }

    @Test
    void 투표진행_시_멤버가_해당_투표_팀_소속이_아니면_예외를_던진다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub2")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        PollItem pollItem2 = pollItems.get(1);

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        Member 차리 = memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), 차리.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"),
                        new PollResultRequest(pollItem2.getId(), "ㅋ"))))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 투표진행시_투표가_팀_소속이_아니면_예외를_던진다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub2")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        PollItem pollItem2 = pollItems.get(1);

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());
        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(invalidTeam.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"),
                        new PollResultRequest(pollItem2.getId(), "ㅋ"))))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 투표진행시_투표가_이미_종료되었다면_예외를_던진다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        poll.addItem(pollItem1);
        poll.close(member);

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"))))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 투표를_진행_시_투표항목이_투표소속이_아니면_예외를_던진다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build()));

        Long invalidPollItemId = pollItems.get(0).getId();

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
                List.of(new PollResultRequest(invalidPollItemId, "그냥뇨"))))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_MISMATCHED_ERROR);
    }

    @Test
    void 없는_투표에는_투표를_할_수_없다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build()));
        String invalidCode = "invalidCode";

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), invalidCode,
                List.of(new PollResultRequest(pollItems.get(0).getId(), "그냥뇨"))))
                .isInstanceOf(PollNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_진행_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        PollItem pollItem1 = PollItem.builder()
                .poll(poll)
                .subject("sub1")
                .build();

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(invalidTeamCode, member.getId(), poll.getCode(),
                List.of(new PollResultRequest(pollItem1.getId(), "그냥뇨"))))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_단건을_조회한다() {
        // when
        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), poll.getCode());

        // then
        assertThat(pollResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new PollResponse(null, poll.getTitle(), poll.getAllowedPollCount(), poll.getIsAnonymous(),
                        poll.getStatus().name(), poll.getCreatedAt(), poll.getClosedAt(), poll.getCode(), true));
    }

    @Test
    void 속하지않은_팀의_투표항목을_조회하면_예외를_던진다() {
        // given
        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());

        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), poll.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 팀에_속하지않은_투표항목을_조회하면_예외를_던진다() {
        // given
        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());
        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));

        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), poll.getCode()))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 투표_단건_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";
        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeamCode, member.getId(), poll.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(
                PollItem.builder()
                        .poll(poll)
                        .subject("sub1")
                        .build(),
                PollItem.builder()
                        .poll(poll)
                        .subject("sub2")
                        .build()));

        PollItem pollItem1 = pollItems.get(0);
        PollItem pollItem2 = pollItems.get(1);

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(),
                poll.getCode());

        // then
        assertThat(pollItemResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(new PollItemResponse(pollItem1.getId(), pollItem1.getSubject(), false, ""),
                                new PollItemResponse(pollItem2.getId(), pollItem2.getSubject(), false, ""))
                );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
                .poll(poll)
                .subject("항목1")
                .build();
        String description = "그냥뇨~";
        pollItem1.addPollResult(member, description);
        PollItem pollItem2 = PollItem.builder()
                .poll(poll)
                .subject("항목2")
                .build();

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(),
                poll.getCode());

        // then
        assertThat(pollItemResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(new PollItemResponse(pollItem1.getId(), pollItem1.getSubject(), true, description),
                                new PollItemResponse(pollItem2.getId(), pollItem2.getSubject(), false, ""))
                );
    }

    @Test
    void 투표_선택_항목_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.findPollItems(invalidTeamCode, member.getId(), poll.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 익명_투표_결과를_조회한다() {
        // given
        Poll anonymousPoll = Poll.builder()
                .team(team)
                .host(member)
                .title("anonymousPoll")
                .allowedPollCount(3)
                .isAnonymous(true)
                .status(OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code(Code.generate(length -> "asdadxxx"))
                .build();
        PollItem pollItem1 = PollItem.builder()
                .poll(anonymousPoll)
                .subject("항목1")
                .build();
        String description1 = "거의_다_한_것_같아요";
        pollItem1.addPollResult(member, description1);
        PollItem pollItem2 = PollItem.builder()
                .poll(anonymousPoll)
                .subject("항목2")
                .build();
        String description2 = "집에_가고_싶어요!";
        pollItem2.addPollResult(member, description2);

        anonymousPoll.addItem(pollItem1);
        anonymousPoll.addItem(pollItem2);

        Poll testPoll = pollRepository.save(anonymousPoll);

        Member anonymous = Member.getAnonymous();
        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(team.getCode(),
                member.getId(), testPoll.getCode());

        // then
        assertThat(pollItemResultResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(new PollItemResultResponse(pollItem1.getId(), 1,
                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
                                                anonymous.getProfileUrl(), description1)), pollItem1.getSubject()),
                                new PollItemResultResponse(pollItem2.getId(), 1,
                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
                                                anonymous.getProfileUrl(), description2)), pollItem2.getSubject()))
                );
    }

    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
                .poll(poll)
                .subject("항목1")
                .build();

        String description1 = "거의_다_한_것_같아요";
        pollItem1.addPollResult(member, description1);

        PollItem pollItem2 = PollItem.builder()
                .poll(poll)
                .subject("항목2")
                .build();

        String description2 = "집에_가고_싶어요!";
        pollItem2.addPollResult(member, description2);

        List<PollItem> pollItems = pollItemRepository.saveAll(List.of(pollItem1, pollItem2));
        poll.addItem(pollItems.get(0));
        poll.addItem(pollItems.get(1));

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(team.getCode(),
                member.getId(), poll.getCode());

        // then
        assertThat(pollItemResultResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(new PollItemResultResponse(pollItem1.getId(), 1,
                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
                                                member.getProfileUrl(), description1)), pollItem1.getSubject()),
                                new PollItemResultResponse(pollItem2.getId(), 1,
                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
                                                member.getProfileUrl(), description2)), pollItem2.getSubject()))
                );
    }

    @Test
    void 투표_결과_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.findPollItemResults(invalidTeamCode, member.getId(), poll.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표를_삭제한다() {
        // when
        pollService.deletePoll(team.getCode(), member.getId(), poll.getCode());

        // then
        assertThatThrownBy(() -> pollService.findPoll(team.getCode(), member.getId(), poll.getCode()))
                .isInstanceOf(PollNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_삭제_시_호스트가_아니면_예외를_던진다() {
        // given
        Member 차리 = memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());

        teamMemberRepository.save(new TeamMember(null, team, 차리));

        // when & then;
        assertThatThrownBy(() -> pollService.deletePoll(team.getCode(), 차리.getId(), poll.getCode()))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
    }

    @Test
    void 투표_삭제_시_팀에_속하지않은_투표를_삭제하면_예외를_던진다() {
        // given
        Team newTeam = teamRepository.save(Team.builder()
                .name("newTeam")
                .code(Code.generate(length -> "123xx111"))
                .build());
        teamMemberRepository.save(new TeamMember(null, newTeam, member));

        // when & then;
        assertThatThrownBy(() -> pollService.deletePoll(newTeam.getCode(), member.getId(), poll.getCode()))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 투표_삭제_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(invalidTeamCode, member.getId(), poll.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표를_종료한다() {
        // when
        pollService.closePoll(team.getCode(), member.getId(), poll.getCode());

        // then
        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED);
    }

    @Test
    void 투표_종료_시_호스트가_아니면_예외를_던진다() {
        // given
        Member 차리 = memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());

        teamMemberRepository.save(new TeamMember(null, team, 차리));

        // when & then
        assertThatThrownBy(() -> pollService.closePoll(team.getCode(), 차리.getId(), poll.getCode()))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
    }

    @Test
    void 투표_마감_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.closePoll(invalidTeamCode, member.getId(), poll.getCode()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }
}
