package com.morak.back.poll.application;

import static com.morak.back.poll.domain.PollStatus.CLOSED;
import static com.morak.back.poll.domain.PollStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.openMocks;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.slack.RestSlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.core.domain.slack.SlackWebhookRepository;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class PollServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollItemRepository pollItemRepository;

    @Mock
    private SlackWebhookRepository slackWebhookRepository;

    @Mock
    private RestSlackClient slackClient;

    private final PollService pollService;

    private Member member;
    private Team team;
    private Poll poll;

    @BeforeEach
    void setup() {
        member = Member.builder()
            .id(1L)
            .oauthId("12345678")
            .name("ellie")
            .profileUrl("http://ellie-profile.com")
            .build();
        team = Team.builder()
            .id(1L)
            .name("team")
            .code(Code.generate(length -> "abcd1234"))
            .build();
        poll = Poll.builder()
            .id(1L)
            .team(team)
            .host(member)
            .title("test-tile")
            .allowedPollCount(3)
            .isAnonymous(true)
            .status(OPEN)
            .closedAt(LocalDateTime.now().plusDays(1L))
            .code(Code.generate(length -> "ABCD1234"))
            .build();
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
    }

    public PollServiceTest() {
        openMocks(this);
        this.pollService = new PollService(
            slackClient,
            pollRepository,
            memberRepository,
            teamRepository,
            teamMemberRepository,
            pollItemRepository,
            slackWebhookRepository
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
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findByCode(team.getCode())).willReturn(Optional.of(team));
        given(pollRepository.save(any(Poll.class))).willReturn(poll);

        // when
        String pollCode = pollService.createPoll(team.getCode(), member.getId(), pollCreateRequest);

        // then
        verify(pollItemRepository).saveAll(any());
        assertThat(pollCode).isEqualTo(poll.getCode());
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findAllByTeamId(anyLong())).willReturn(List.of(poll));

        // when
        List<PollResponse> polls = pollService.findPolls(team.getCode(), member.getId());

        // then
        Assertions.assertAll(
            () -> assertThat(polls).allMatch(response -> response.getStatus().equals("OPEN")),
            () -> assertThat(polls).allMatch(PollResponse::getIsHost)
        );
    }

    @Test
    void 투표를_진행한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .poll(poll)
            .subject("sub1")
            .build();
        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .poll(poll)
            .subject("sub2")
            .build();
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        given(pollItemRepository.findById(pollItem1.getId())).willReturn(Optional.of(pollItem1));
        given(pollItemRepository.findById(pollItem2.getId())).willReturn(Optional.of(pollItem2));

        // when
        pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
            List.of(new PollResultRequest(1L, "그냥뇨"), new PollResultRequest(2L, "ㅋ")));

        // then
        Assertions.assertAll(
            () -> assertThat(pollItem1.getPollResults()).hasSize(1),
            () -> assertThat(pollItem2.getPollResults()).hasSize(1)
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .subject("sub1")
            .build();
        pollItem1.addPollResult(member, "거의_다_한_것_같아요");
        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .subject("sub2")
            .build();
        pollItem1.addPollResult(member, "집에_가고_싶어요");
        PollItem pollItem3 = PollItem.builder()
            .id(3L)
            .subject("sub3")
            .build();

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);
        poll.addItem(pollItem3);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        given(pollItemRepository.findById(pollItem1.getId())).willReturn(Optional.of(pollItem1));
        given(pollItemRepository.findById(pollItem2.getId())).willReturn(Optional.of(pollItem2));
        given(pollItemRepository.findById(pollItem3.getId())).willReturn(Optional.of(pollItem3));

        // when
        pollService.doPoll(team.getCode(), member.getId(), poll.getCode(),
            List.of(new PollResultRequest(pollItem2.getId(), "하기싫다."),
                new PollResultRequest(pollItem3.getId(), "테스트 수정")
            ));

        // then
        Assertions.assertAll(
            () -> assertThat(pollItem1.getPollResults()).hasSize(0),
            () -> assertThat(pollItem2.getPollResults()).hasSize(1),
            () -> assertThat(pollItem3.getPollResults()).hasSize(1)
        );
    }

    @Test
    void 투표_단건을_조회한다() {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        PollResponse pollResponse = pollService.findPoll(team.getCode(), member.getId(), poll.getCode());

        // then
        Assertions.assertAll(
            () -> assertThat(pollResponse.getTitle()).isEqualTo(poll.getTitle()),
            () -> assertThat(pollResponse.getIsHost()).isTrue()
        );
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .poll(poll)
            .subject("sub1")
            .build();
        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .poll(poll)
            .subject("sub2")
            .build();
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(),
            poll.getCode());

        // then
        Assertions.assertAll(
            () -> assertThat(pollItemResponses).hasSize(2),
            () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo(pollItem1.getSubject()),
            () -> assertThat(pollItemResponses.get(0).getSelected()).isFalse(),
            () -> assertThat(pollItemResponses.get(0).getDescription()).isBlank()
        );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .subject("항목1")
            .build();
        pollItem1.addPollResult(member, "그냥뇨~");
        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .subject("항목2")
            .build();

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(team.getCode(), member.getId(),
            poll.getCode());

        // then
        Assertions.assertAll(
            () -> assertThat(pollItemResponses).hasSize(2),
            () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1"),
            () -> assertThat(pollItemResponses.get(0).getSelected()).isTrue(),
            () -> assertThat(pollItemResponses.get(0).getDescription()).isEqualTo("그냥뇨~")
        );
    }

    @Test
    void 익명_투표_결과를_조회한다() {
        // given
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .poll(poll)
            .subject("항목1")
            .build();
        pollItem1.addPollResult(member, "거의_다_한_것_같아요");
        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .poll(poll)
            .subject("항목2")
            .build();
        pollItem2.addPollResult(member, "집에_가고_싶어요!");

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(1L));

        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(team.getCode(),
            member.getId(), poll.getCode());

        // then
        Assertions.assertAll(
            () -> assertThat(pollItemResultResponses).hasSize(2),
            () -> assertThat(pollItemResultResponses.get(0).getCount()).isEqualTo(1),
            () -> assertThat(pollItemResultResponses.get(0).getMembers()).hasSize(1),
            () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getId()).isEqualTo(0L),
            () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getName()).isEqualTo(
                Member.getAnonymous().getName()),
            () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo(
                "거의_다_한_것_같아요")
        );
    }

    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        Poll poll = Poll.builder()
            .id(1L)
            .team(team)
            .host(member)
            .isAnonymous(false)
            .status(CLOSED)
            .code(Code.generate(length -> "abcd1234"))
            .build();
        PollItem pollItem1 = PollItem.builder()
            .id(1L)
            .poll(poll)
            .subject("항목1")
            .build();
        pollItem1.addPollResult(member, "거의_다_한_것_같아요");

        PollItem pollItem2 = PollItem.builder()
            .id(2L)
            .poll(poll)
            .subject("항목2")
            .build();
        pollItem2.addPollResult(member, "집에_가고_싶어요!");

        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));

        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(team.getCode(),
            member.getId(), poll.getCode());

        // then
        Assertions.assertAll(
            () -> assertThat(pollItemResultResponses).hasSize(2),
            () -> assertThat(pollItemResultResponses.get(0).getMembers()).hasSize(1),
            () -> assertThat(pollItemResultResponses.get(0).getCount()).isEqualTo(1)
        );
    }

    @Test
    void 투표를_삭제한다() {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when
        pollService.deletePoll(team.getCode(), member.getId(), poll.getCode());

        // then
        verify(pollRepository).deleteById(poll.getId());
    }

    @Test
    void 삭제_시_호스트가_아니면_예외를_던진다() {
        // given
        Member notHostMember = Member.builder()
            .id(2L)
            .oauthId("87654321")
            .name("eden")
            .profileUrl("http://eden-profile.cloudfront.net")
            .build();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(notHostMember));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));

        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(team.getCode(), notHostMember.getId(), poll.getCode()))
            .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 투표를_종료한다() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(team.getCode())).willReturn(Optional.of(team.getId()));
        given(pollRepository.findByCodeAndTeamId(anyString(), anyLong())).willReturn(Optional.of(poll));
        given(slackWebhookRepository.findByTeamId(anyLong())).willReturn(Optional.of(new SlackWebhook()));
        doNothing().when(slackClient).notifyClosed(any(SlackWebhook.class), anyString());

        // when
        pollService.closePoll(team.getCode(), member.getId(), poll.getCode());

        // then
        assertThat(poll.getStatus()).isEqualTo(CLOSED);
        verify(slackWebhookRepository).findByTeamId(anyLong());
    }
}
