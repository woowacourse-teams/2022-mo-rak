package com.morak.back.poll.application;

import static com.morak.back.poll.domain.PollStatus.CLOSED;
import static com.morak.back.poll.domain.PollStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.application.dto.MemberResultResponse;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.poll.exception.PollItemNotFoundException;
import com.morak.back.poll.exception.PollNotFoundException;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PollServiceTest {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final PollService pollService;

    @Autowired
    public PollServiceTest(
            MemberRepository memberRepository,
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            PollService pollService
    ) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.pollService = pollService;
    }

    private Member member;
    private Team team;
    private PollCreateRequest pollCreateRequest;

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
        PollResponse pollResponse = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(pollResponse.getTitle()).isEqualTo(pollCreateRequest.getTitle()),
                () -> assertThat(pollResponse.getAllowedPollCount()).isEqualTo(pollCreateRequest.getAllowedPollCount()),
                () -> assertThat(pollResponse.getIsAnonymous()).isEqualTo(pollCreateRequest.getAnonymous()),
                () -> assertThat(pollResponse.getClosedAt()).isEqualTo(pollCreateRequest.getClosedAt()),
                () -> assertThat(pollResponse.getCode()).isNotNull()
        );
    }

    @Test
    void 투표를_생성_시_멤버가_팀에_속해있지_않는_경우_예외를_던진다() {
        // given
        Member 차리 = saveOtherMember();

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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

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
                                pollCreateRequest.getAnonymous(),
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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        PollCreateRequest pollCreateRequest2 = new PollCreateRequest(
                "title2",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("항목1", "항목2")
        );

        String pollCode2 = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest2).getCode();

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
                                        pollCreateRequest2.getAnonymous(),
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
                                        pollCreateRequest.getAnonymous(),
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
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        PollCreateRequest pollCreateRequest1 = new PollCreateRequest(
                "order2",
                1,
                false,
                LocalDateTime.now().plusDays(1),
                List.of("item1", "item2")
        );

        String pollCode1 = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest1).getCode();

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
                                        pollCreateRequest1.getAnonymous(),
                                        CLOSED.name(),
                                        null,
                                        pollCreateRequest1.getClosedAt(),
                                        pollCode1,
                                        true,
                                        0),
                                new PollResponse(null,
                                        pollCreateRequest.getTitle(),
                                        pollCreateRequest.getAllowedPollCount(),
                                        pollCreateRequest.getAnonymous(),
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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        Member otherMember = saveOtherMember();
        teamMemberRepository.save(new TeamMember(null, team, otherMember));

        // when
        List<PollResponse> polls = pollService.findPolls(team.getCode(), otherMember.getId());

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
                                        pollCreateRequest.getAnonymous(),
                                        OPEN.name(),
                                        null,
                                        pollCreateRequest.getClosedAt(),
                                        pollCode,
                                        false,
                                        0)
                        )
                );
    }

    private Member saveOtherMember() {
        return memberRepository.save(Member.builder()
                .oauthId("leechari")
                .name("이찬주")
                .profileUrl("http://lee-profile.com")
                .build());
    }

    @Test
    void 투표_목록_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

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

    @Test
    void 투표진행_시_멤버가_해당_투표_팀_소속이_아니면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        Member otherMember = saveOtherMember();

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), otherMember.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥뇨"),
                        new PollResultRequest(2L, "ㅋ"))))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 투표진행시_투표가_팀_소속이_아니면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());
        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(invalidTeam.getCode(), member.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥뇨"),
                        new PollResultRequest(2L, "ㅋ"))))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 투표진행시_투표가_이미_종료되었다면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        pollService.closePoll(team.getCode(), member.getId(), pollCode);

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥뇨"))))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 투표를_진행_시_투표항목이_투표소속이_아니면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), pollCode,
                List.of(new PollResultRequest(4L, "그냥뇨"))))
                .isInstanceOf(PollItemNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR);
    }

    @Test
    void 없는_투표에는_투표를_할_수_없다() {
        // given
        String invalidCode = "invalidCode";

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(team.getCode(), member.getId(), invalidCode,
                List.of(new PollResultRequest(1L, "그냥뇨"))))
                .isInstanceOf(PollNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_진행_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.doPoll(invalidTeamCode, member.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥뇨"))))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_단건을_조회한다() {
        // when
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), pollCode);

        // then
        assertThat(pollResponse)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(new PollResponse(
                        null,
                        pollCreateRequest.getTitle(),
                        pollCreateRequest.getAllowedPollCount(),
                        pollCreateRequest.getAnonymous(),
                        OPEN.name(),
                        LocalDateTime.now(),
                        pollCreateRequest.getClosedAt(),
                        pollCode,
                        true,
                        0
                ));
    }

    @Test
    void 투표_진행_후_단건_조회_시_count값이_반영된다(@Autowired EntityManager entityManager) {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        pollService.doPoll(team.getCode(), member.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥뇨"),
                        new PollResultRequest(2L, "저스트 그냥!")));

        Member 엘리 = memberRepository.save(Member.builder()
                .oauthId("ellieHan")
                .name("한해리")
                .profileUrl("http://han-profile.com")
                .build());
        teamMemberRepository.save(new TeamMember(null, team, 엘리));

        pollService.doPoll(team.getCode(), 엘리.getId(), pollCode,
                List.of(new PollResultRequest(1L, "그냥그냥그냐앙~")));

        entityManager.flush();

        // when
        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), pollCode);

        // then
        assertThat(pollResponse)
                .extracting("code", "count")
                .containsExactly(pollCode, 2);
    }

    @Test
    void 속하지않은_팀의_투표를_조회하면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());

        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), pollCode))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 팀에_속하지않은_투표항목을_조회하면_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        Team invalidTeam = teamRepository.save(Team.builder()
                .name("invalidTeam")
                .code(Code.generate(length -> "12341234"))
                .build());
        teamMemberRepository.save(new TeamMember(null, invalidTeam, member));

        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeam.getCode(), member.getId(), pollCode))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    //
    @Test
    void 투표_단건_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        String invalidTeamCode = "kingEden";
        // when & then
        assertThatThrownBy(() -> pollService.findPoll(invalidTeamCode, member.getId(), pollCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(), pollCode);

        // then
        assertThat(pollItemResponses)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        List.of(new PollItemResponse(null, "삼겹살", false, ""),
                                new PollItemResponse(null, "회", false, ""),
                                new PollItemResponse(null, "쌈밥정식", false, ""))
                );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        List<PollResultRequest> pollResultRequests = List.of(
                new PollResultRequest(1L, "그냥뇨"),
                new PollResultRequest(2L, "하하하"),
                new PollResultRequest(3L, "ㅋ"));
        pollService.doPoll(team.getCode(), member.getId(), pollCode, pollResultRequests);

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(), pollCode);

        // then
        assertThat(pollItemResponses)
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
                                        true,
                                        "하하하"
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

    @Test
    void 투표_선택_항목_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();
        String invalidTeamCode = "kingEden";

        // when & then
        assertThatThrownBy(() -> pollService.findPollItems(invalidTeamCode, member.getId(), pollCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 익명_투표_결과를_조회한다() {
        // given
        PollCreateRequest anonymousPollCreateRequest = new PollCreateRequest(
                "모락 회식",
                3,
                true,
                LocalDateTime.now().plusDays(1),
                List.of("삼겹살", "회", "쌈밥정식")
        );

        String pollCode = pollService.createPoll(team.getCode(), member.getId(), anonymousPollCreateRequest).getCode();

        // when
        List<PollResultRequest> pollResultRequests = List.of(
                new PollResultRequest(1L, "그냥뇨"),
                new PollResultRequest(3L, "ㅋ"));
        pollService.doPoll(team.getCode(), member.getId(), pollCode, pollResultRequests);
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollResults(team.getCode(),
                member.getId(), pollCode);
        Member anonymous = Member.getAnonymousMember();

        // then
        assertThat(pollItemResultResponses)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        List.of(new PollItemResultResponse(null, 1,
                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
                                                anonymous.getProfileUrl(), "그냥뇨")), "삼겹살"),
                                new PollItemResultResponse(null, 0,
                                        new ArrayList<>(), "회"),
                                new PollItemResultResponse(null, 1,
                                        List.of(new MemberResultResponse(anonymous.getId(), anonymous.getName(),
                                                anonymous.getProfileUrl(), "ㅋ")), "쌈밥정식"))
                );
    }

    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        List<PollItemResponse> pollItems = pollService.findPollItems(team.getCode(), member.getId(), pollCode);
        PollItemResponse itemA = pollItems.get(0);
        PollItemResponse itemB = pollItems.get(1);
        PollItemResponse itemC = pollItems.get(2);

        PollResultRequest selectRequestA = new PollResultRequest(itemA.getId(), "그냥뇨");
        PollResultRequest selectRequestB = new PollResultRequest(itemB.getId(), "ㅋ");
        pollService.doPoll(team.getCode(), member.getId(), pollCode, List.of(selectRequestA, selectRequestB));

        // when
        List<PollItemResultResponse> pollResults = pollService.findPollResults(team.getCode(), member.getId(),
                pollCode);

        // then
        assertThat(pollResults)
                .usingRecursiveComparison()
                .ignoringFields("subject")
                .isEqualTo(
                        List.of(new PollItemResultResponse(itemA.getId(), 1,
                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
                                                member.getProfileUrl(), selectRequestA.getDescription())), null),
                                new PollItemResultResponse(itemB.getId(), 1,
                                        List.of(new MemberResultResponse(member.getId(), member.getName(),
                                                member.getProfileUrl(), selectRequestB.getDescription())), null),
                                new PollItemResultResponse(itemC.getId(), 0, List.of(), null))
                );
    }

    @Test
    void 투표_결과_조회_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when & then
        assertThatThrownBy(
                () -> pollService.doPoll(invalidTeamCode, member.getId(), pollCode, List.of(new PollResultRequest())))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표를_삭제한다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when
        pollService.deletePoll(team.getCode(), member.getId(), pollCode);

        // then
        assertThatThrownBy(() -> pollService.findPoll(team.getCode(), member.getId(), pollCode))
                .isInstanceOf(PollNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_NOT_FOUND_ERROR);
    }

    @Test
    void 투표_삭제_시_호스트가_아니면_예외를_던진다() {
        // given
        Member otherMember = saveOtherMember();
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        teamMemberRepository.save(new TeamMember(null, team, otherMember));

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(team.getCode(), otherMember.getId(), pollCode))
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
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(newTeam.getCode(), member.getId(), pollCode))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR);
    }

    @Test
    void 투표_삭제_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(invalidTeamCode, member.getId(), pollCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 투표를_종료한다() {
        // given
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when
        pollService.closePoll(team.getCode(), member.getId(), pollCode);

        // then
        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), pollCode);

        assertThat(pollResponse.getStatus()).isEqualTo(PollStatus.CLOSED.name());
    }

    @Test
    void 투표_종료_시_호스트가_아니면_예외를_던진다() {
        // given
        Member otherMember = saveOtherMember();
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        teamMemberRepository.save(new TeamMember(null, team, otherMember));

        // when & then
        assertThatThrownBy(() -> pollService.closePoll(team.getCode(), otherMember.getId(), pollCode))
                .isInstanceOf(AuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.HOST_MISMATCHED_ERROR);
    }

    @Test
    void 투표_마감_시_팀이_존재하지_않는_경우_예외를_던진다() {
        // given
        String invalidTeamCode = "kingEden";
        String pollCode = 투표를_초기화하고_코드를_받아온다();

        // when & then
        assertThatThrownBy(() -> pollService.closePoll(invalidTeamCode, member.getId(), pollCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    private String 투표를_초기화하고_코드를_받아온다() {
        return pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest).getCode();
    }
}
