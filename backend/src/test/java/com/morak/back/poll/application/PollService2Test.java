package com.morak.back.poll.application;

import static com.morak.back.poll.domain.PollStatus.CLOSED;
import static com.morak.back.poll.domain.PollStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.repository.NewPollRepository;
import com.morak.back.brandnew.service.NewPollService;
import com.morak.back.core.application.NotificationService;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.slack.FakeApiReceiver;
import com.morak.back.core.domain.slack.FakeSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.support.ServiceTest;
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

@ServiceTest
class PollService2Test {

    // 방법1 레포지토리 만들어서 넣기
    // 방법2 entity manager 로 넣기
    // 방법3 service 로 넣기
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final NewPollRepository pollRepository;
    private final PollItemRepository newPollItemRepository;
    private final FakeApiReceiver receiver;
    private final SlackClient slackClient;

    private final NotificationService notificationService;
    private final NewPollService pollService;

    private Member member;
    private Team team;
    private PollCreateRequest pollCreateRequest;

    @Autowired
    public PollService2Test(MemberRepository memberRepository, TeamRepository teamRepository,
                            TeamMemberRepository teamMemberRepository, NewPollRepository pollRepository,
                            PollItemRepository newPollItemRepository,
                            SlackWebhookRepository slackWebhookRepository) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.pollRepository = pollRepository;
        this.newPollItemRepository = newPollItemRepository;
        this.receiver = new FakeApiReceiver();
        this.slackClient = new FakeSlackClient(receiver);
        this.notificationService = new NotificationService(slackClient, teamRepository, teamMemberRepository,
                slackWebhookRepository, memberRepository);
        this.pollService = new NewPollService(
                memberRepository,
                teamRepository,
                teamMemberRepository,
                pollRepository
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
        teamMemberRepository.save(new TeamMember(null, team, member));

        pollCreateRequest = new PollCreateRequest(
                "모락 회식",
                3,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("삼겹살", "회", "쌈밥정식")
        );
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

        // when & then
        assertThatThrownBy(() -> pollService.createPoll(invalidTeamCode, member.getId(), pollCreateRequest))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        String pollCode = 투표_초기화_상태();

        // when
        List<PollResponse> polls = pollService.findPolls(team.getCode(), member.getId());

        // then
        assertThat(polls)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(
                        List.of(new PollResponse(
                                null,
                                pollCreateRequest.getTitle(),
                                pollCreateRequest.getAllowedPollCount(),
                                pollCreateRequest.getIsAnonymous(),
                                OPEN.name(),
                                null,
                                pollCreateRequest.getClosedAt(),
                                pollCode,
                                true,
                                0)
                        )
                );
    }

    @Test
    void 투표_목록을_조회할_때_진행중인_투표가_종료된_투표보다_먼저_출력된다() {
        // given
        String pollCode = 투표_초기화_상태();

        PollCreateRequest pollCreateRequest2 = new PollCreateRequest(
                "title2",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2")
        );

        String pollCode2 = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest2);

        // when
        pollService.closePoll(team.getCode(), member.getId(), pollCode);
        List<PollResponse> polls = pollService.findPolls(team.getCode(), member.getId());

        // then
        assertThat(polls)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(
                        List.of(
                                new PollResponse(
                                        null,
                                        pollCreateRequest2.getTitle(),
                                        pollCreateRequest2.getAllowedPollCount(),
                                        pollCreateRequest2.getIsAnonymous(),
                                        OPEN.name(),
                                        null,
                                        pollCreateRequest2.getClosedAt(),
                                        pollCode2,
                                        true,
                                        0),
                                new PollResponse(
                                        null,
                                        pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getIsAnonymous(),
                                        CLOSED.name(),
                                        null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        true,
                                        0)
                        ));
    }

    @Test
    void 투표_목록을_조회할_때_종료_상태가_같으면_생성된_순서대로_출력된다() {
        // given
        String pollCode = 투표_초기화_상태();
        PollCreateRequest pollCreateRequest1 = new PollCreateRequest(
                "order2",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        String pollCode1 = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest1);

        // when
        pollService.closePoll(team.getCode(), member.getId(), pollCode);
        pollService.closePoll(team.getCode(), member.getId(), pollCode1);
        List<PollResponse> polls = pollService.findPolls(team.getCode(), member.getId());

        // then
        assertThat(polls)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(
                        List.of(
                                new PollResponse(
                                        null,
                                        pollCreateRequest1.getTitle(),
                                        pollCreateRequest1.getAllowedPollCount(),
                                        pollCreateRequest1.getIsAnonymous(),
                                        CLOSED.name(),
                                        null,
                                        pollCreateRequest1.getClosedAt(),
                                        pollCode1,
                                        true,
                                        0),
                                new PollResponse(null,
                                        pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getIsAnonymous(),
                                        CLOSED.name(),
                                        null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        true,
                                        0)
                        )
                );
    }

    @Test
    void 투표의_주인이_아닌_유저가_투표_목록을_조회할_수_있다() {
        // given
        String pollCode = 투표_초기화_상태();

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
                        List.of(
                                new PollResponse(
                                        null,
                                        pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getIsAnonymous(),
                                        OPEN.name(),
                                        null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        false,
                                        0)
                        )
                );
    }

    @Test
    void 투표_목록_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String pollCode = 투표_초기화_상태();
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
        String pollCode = 투표_초기화_상태();

        // when
        List<PollResultRequest> pollResultRequests = List.of(new PollResultRequest(1L, "그냥뇨"),
                new PollResultRequest(2L, "ㅋ"));
        pollService.doPoll(team.getCode(), member.getId(), pollCode, pollResultRequests);

        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), pollCode);
        // then
        assertThat(pollResponse.getCount()).isEqualTo(1);
    }

    @Test
    void 재투표를_진행한다() {
        // given
        String pollCode = 투표_초기화_상태();

        // when
        List<PollResultRequest> firstPollResultRequests = List.of(
                new PollResultRequest(1L, "그냥뇨"),
                new PollResultRequest(2L, "하하하"),
                new PollResultRequest(3L, "ㅋ"));
        List<PollResultRequest> secondPollResultRequests = List.of(
                new PollResultRequest(1L, "그냥뇨"),
                new PollResultRequest(3L, "ㅋ"));
        pollService.doPoll(team.getCode(), member.getId(), pollCode, firstPollResultRequests);
        pollService.doPoll(team.getCode(), member.getId(), pollCode, secondPollResultRequests);

        List<PollItemResponse> pollItems = pollService.findPollItems(team.getCode(), member.getId(), pollCode);
        // then
        assertThat(pollItems)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        List.of(
                                new PollItemResponse(
                                        null,
                                        "삼겹살",
                                        true,
                                        "그냥뇨"
                                ),
                                new PollItemResponse(
                                        null,
                                        "회",
                                        false,
                                        ""
                                ),
                                new PollItemResponse(
                                        null,
                                        "쌈밥정식",
                                        true,
                                        "ㅋ"
                                )
                        )
                );
    }

//    @Test
//    void 투표진행_시_멤버가_해당_투표_팀_소속이_아니면_예외를_던진다() {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build(),
//                NewPollItem.builder()
//
//                        .subject("sub2")
//                        .build()));
//
//        NewPollItem newPollItem1 = newPollItems.get(0);
//        NewPollItem newPollItem2 = newPollItems.get(1);
//
//        Member 차리 = memberRepository.save(Member.builder()
//                .oauthId("leechari")
//                .name("이찬주")
//                .profileUrl("http://lee-profile.com")
//                .build());
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), 차리.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥뇨"),
//                        new PollResultRequest(newPollItem2.getId(), "ㅋ"))))
//                .isInstanceOf(TeamAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표진행시_투표가_팀_소속이_아니면_예외를_던진다() {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build(),
//                NewPollItem.builder()
//
//                        .subject("sub2")
//                        .build()));
//
//        NewPollItem newPollItem1 = newPollItems.get(0);
//        NewPollItem newPollItem2 = newPollItems.get(1);
//
//        Team invalidTeam = teamRepository.save(Team.builder()
//                .name("invalidTeam")
//                .code(Code.generate(length -> "12341234"))
//                .build());
//        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(invalidTeam.getCode(), member.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥뇨"),
//                        new PollResultRequest(newPollItem2.getId(), "ㅋ"))))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표진행시_투표가_이미_종료되었다면_예외를_던진다() {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build()));
//
//        NewPollItem newPollItem1 = newPollItems.get(0);
//        poll.close(member.getId());
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥뇨"))))
//                .isInstanceOf(PollDomainLogicException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
//    }
//
//    @Test
//    void 투표를_진행_시_투표항목이_투표소속이_아니면_예외를_던진다() {
//        // given
//        NewPoll otherPoll = pollRepository.save(
//                NewPoll.builder()
//                        .pollInfo(
//                                PollInfo.builder()
//                                        .hostId(member.getId())
//                                        .title("test-tile")
//                                        .allowedCount(3)
//                                        .anonymous(false)
//                                        .status(OPEN)
//                                        .closedAt(new TempDateTime(LocalDateTime.now().plusDays(1)))
//                                        .build()
//                        )
//                        .build());
//        );
//
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//                        .poll(otherPoll)
//                        .subject("sub1")
//                        .build()));
//
//        Long invalidNewPollItemId = newPollItems.get(0).getId();
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(invalidNewPollItemId, "그냥뇨"))))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_ITEM_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 없는_투표에는_투표를_할_수_없다() {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build()));
//        String invalidCode = "invalidCode";
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), invalidCode,
//                List.of(new PollResultRequest(newPollItems.get(0).getId(), "그냥뇨"))))
//                .isInstanceOf(PollNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표_진행_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//
//        NewPollItem newPollItem1 = NewPollItem.builder()
//
//                .subject("sub1")
//                .build();
//
//        // when & then
//        assertThatThrownBy(() -> pollService.doPoll(invalidTeamCode, member.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥뇨"))))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표_단건을_조회한다() {
//        // when
//        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode());
//
//        // then
//        assertThat(pollResponse)
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(new PollResponse(null, poll.getTitle(), poll.getAllowedPollCount(), poll.getIsAnonymous(),
//                        poll.getStatus().name(), poll.getCreatedAt(),
//                        poll.getClosedAt(), poll.getPollInfo().getCode(), true, 0));
//    }
//
//    @Test
//    void 투표_진행_후_단건_조회_시_count값이_반영된다(@Autowired EntityManager entityManager) {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build(),
//                NewPollItem.builder()
//
//                        .subject("sub2")
//                        .build()));
//
//        NewPollItem newPollItem1 = newPollItems.get(0);
//        NewPollItem newPollItem2 = newPollItems.get(1);
//
//        pollService.doPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥뇨"),
//                        new PollResultRequest(newPollItem2.getId(), "저스트 그냥!")));
//
//        Member 엘리 = memberRepository.save(Member.builder()
//                .oauthId("ellieHan")
//                .name("한해리")
//                .profileUrl("http://han-profile.com")
//                .build());
//        teamMemberRepository.save(new TeamMember(null, team, 엘리));
//
//        pollService.doPoll(team.getCode(), 엘리.getId(), poll.getPollInfo().getCode(),
//                List.of(new PollResultRequest(newPollItem1.getId(), "그냥그냥그냐앙~")));
//
//        entityManager.flush();
//        entityManager.detach(poll);
//
//        // when
//        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode());
//
//        // then
//        assertThat(pollResponse)
//                .extracting("id", "count")
//                .containsExactly(poll.getId(), 2);
//    }
//
//    @Test
//    void 속하지않은_팀의_투표를_조회하면_예외를_던진다() {
//        // given
//        Team invalidTeam = teamRepository.save(Team.builder()
//                .name("invalidTeam")
//                .code(Code.generate(length -> "12341234"))
//                .build());
//
//        // when & then
//        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 팀에_속하지않은_투표항목을_조회하면_예외를_던진다() {
//        // given
//        Team invalidTeam = teamRepository.save(Team.builder()
//                .name("invalidTeam")
//                .code(Code.generate(length -> "12341234"))
//                .build());
//        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));
//
//        // when & then
//        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표_단건_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//        // when & then
//        assertThatThrownBy(() -> pollService.findPoll(invalidTeamCode, member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표_선택_항목을_조회한다() {
//        // given
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(
//                NewPollItem.builder()
//
//                        .subject("sub1")
//                        .build(),
//                NewPollItem.builder()
//
//                        .subject("sub2")
//                        .build()));
//
//        NewPollItem newPollItem1 = newPollItems.get(0);
//        NewPollItem newPollItem2 = newPollItems.get(1);
//
//        // when
//        List<NewPollItemResponse> newPollItemResponses = pollService.findNewPollItems(team.getCode(), member.getId(),
//                poll.getPollInfo().getCode());
//
//        // then
//        assertThat(newPollItemResponses)
//                .usingRecursiveComparison()
//                .isEqualTo(
//                        List.of(new NewPollItemResponse(newPollItem1.getId(), newPollItem1.getSubject(), false, ""),
//                                new NewPollItemResponse(newPollItem2.getId(), newPollItem2.getSubject(), false, ""))
//                );
//    }
//
//    @Test
//    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
//        // given
//        NewPollItem newPollItem1 = NewPollItem.builder()
//
//                .subject("항목1")
//                .build();
//        String description = "그냥뇨~";
//        newPollItem1.addPollResult(member, description);
//        NewPollItem newPollItem2 = NewPollItem.builder()
//
//                .subject("항목2")
//                .build();
//
//        // when
//        List<NewPollItemResponse> newPollItemResponses = pollService.findNewPollItems(team.getCode(), member.getId(),
//                poll.getPollInfo().getCode());
//
//        // then
//        assertThat(newPollItemResponses)
//                .usingRecursiveComparison()
//                .isEqualTo(
//                        List.of(new NewPollItemResponse(newPollItem1.getId(), newPollItem1.getSubject(), true,
//                                        description),
//                                new NewPollItemResponse(newPollItem2.getId(), newPollItem2.getSubject(), false, ""))
//                );
//    }
//
//    @Test
//    void 투표_선택_항목_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//
//        // when & then
//        assertThatThrownBy(() -> pollService.findNewPollItems(invalidTeamCode, member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 익명_투표_결과를_조회한다() {
//        // given
//        Poll anonymousPoll = Poll.builder()
//                .team(team)
//                .host(member)
//                .title("anonymousPoll")
//                .allowedPollCount(3)
//                .isAnonymous(true)
//                .status(OPEN)
//                .closedAt(LocalDateTime.now().plusDays(1L))
//                .code(Code.generate(length -> "asdadxxx"))
//                .build();
//        NewPollItem newPollItem1 = NewPollItem.builder()
//                .poll(anonymousPoll)
//                .subject("항목1")
//                .build();
//        String description1 = "거의_다_한_것_같아요";
//        newPollItem1.addPollResult(member, description1);
//        NewPollItem newPollItem2 = NewPollItem.builder()
//                .poll(anonymousPoll)
//                .subject("항목2")
//                .build();
//        String description2 = "집에_가고_싶어요!";
//        newPollItem2.addPollResult(member, description2);
//
//        Poll testPoll = pollRepository.save(anonymousPoll);
//
//        Member anonymous = Member.getAnonymous();
//        // when
//        List<NewPollItemResultResponse> newPollItemResultResponses = pollService.findNewPollItemResults(team.getCode(),
//                member.getId(), testPoll.getCode());
//
//        // then
//        assertThat(newPollItemResultResponses)
//                .usingRecursiveComparison()
//                .isEqualTo(
//                        List.of(new NewPollItemResultResponse(newPollItem1.getId(), 1,
//                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
//                                                anonymous.getProfileUrl(), description1)), newPollItem1.getSubject()),
//                                new NewPollItemResultResponse(newPollItem2.getId(), 1,
//                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
//                                                anonymous.getProfileUrl(), description2)), newPollItem2.getSubject()))
//                );
//    }
//
//    @Test
//    void 기명_투표_결과를_조회한다() {
//        // given
//        NewPollItem newPollItem1 = NewPollItem.builder()
//
//                .subject("항목1")
//                .build();
//
//        String description1 = "거의_다_한_것_같아요";
//        newPollItem1.addPollResult(member, description1);
//
//        NewPollItem newPollItem2 = NewPollItem.builder()
//
//                .subject("항목2")
//                .build();
//
//        String description2 = "집에_가고_싶어요!";
//        newPollItem2.addPollResult(member, description2);
//
//        List<NewPollItem> newPollItems = newPollItemRepository.saveAll(List.of(newPollItem1, newPollItem2));
//
//        // when
//        List<NewPollItemResultResponse> newPollItemResultResponses = pollService.findNewPollItemResults(team.getCode(),
//                member.getId(), poll.getPollInfo().getCode());
//
//        // then
//        assertThat(newPollItemResultResponses)
//                .usingRecursiveComparison()
//                .isEqualTo(
//                        List.of(new NewPollItemResultResponse(newPollItem1.getId(), 1,
//                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
//                                                member.getProfileUrl(), description1)), newPollItem1.getSubject()),
//                                new NewPollItemResultResponse(newPollItem2.getId(), 1,
//                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
//                                                member.getProfileUrl(), description2)), newPollItem2.getSubject()))
//                );
//    }
//
//    @Test
//    void 투표_결과_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//
//        // when & then
//        assertThatThrownBy(() -> pollService.findNewPollItemResults(invalidTeamCode, member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표를_삭제한다() {
//        // when
//        pollService.deletePoll(team.getCode(), member.getId(), poll.getPollInfo().getCode());
//
//        // then
//        assertThatThrownBy(() -> pollService.findPoll(team.getCode(), member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(PollNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표_삭제_시_호스트가_아니면_예외를_던진다() {
//        // given
//        Member 차리 = memberRepository.save(Member.builder()
//                .oauthId("leechari")
//                .name("이찬주")
//                .profileUrl("http://lee-profile.com")
//                .build());
//
//        teamMemberRepository.save(new TeamMember(null, team, 차리));
//
//        // when & then;
//        assertThatThrownBy(() -> pollService.deletePoll(team.getCode(), 차리.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표_삭제_시_팀에_속하지않은_투표를_삭제하면_예외를_던진다() {
//        // given
//        Team newTeam = teamRepository.save(Team.builder()
//                .name("newTeam")
//                .code(Code.generate(length -> "123xx111"))
//                .build());
//        teamMemberRepository.save(new TeamMember(null, newTeam, member));
//
//        // when & then;
//        assertThatThrownBy(() -> pollService.deletePoll(newTeam.getCode(), member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표_삭제_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//
//        // when & then
//        assertThatThrownBy(() -> pollService.deletePoll(invalidTeamCode, member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }
//
//    @Test
//    void 투표를_종료한다() {
//        // when
//        pollService.closePoll(team.getCode(), member.getId(), poll.getPollInfo().getCode());
//
//        // then
//        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED);
//    }
//
//    @Test
//    void 투표_종료_시_호스트가_아니면_예외를_던진다() {
//        // given
//        Member 차리 = memberRepository.save(Member.builder()
//                .oauthId("leechari")
//                .name("이찬주")
//                .profileUrl("http://lee-profile.com")
//                .build());
//
//        teamMemberRepository.save(new TeamMember(null, team, 차리));
//
//        // when & then
//        assertThatThrownBy(() -> pollService.closePoll(team.getCode(), 차리.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(PollAuthorizationException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
//    }
//
//    @Test
//    void 투표_마감_시_팀이_존재하지_않는_경우_예외를_던진다() {
//        // given
//        String invalidTeamCode = "kingEden";
//
//        // when & then
//        assertThatThrownBy(() -> pollService.closePoll(invalidTeamCode, member.getId(), poll.getPollInfo().getCode()))
//                .isInstanceOf(TeamNotFoundException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
//    }

    private String 투표_초기화_상태() {
        return pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);
    }
}
