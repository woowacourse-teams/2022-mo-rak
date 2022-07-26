package com.morak.back.poll.application;

import static com.morak.back.poll.domain.PollStatus.CLOSED;
import static com.morak.back.poll.domain.PollStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollResult;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final PollService pollService;

    private Long memberId = 1L;
    private String teamCode = "morakmor";
    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(1L, "12345678", "ellie", "ellie-profile");
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong()))
                .willReturn(true);
    }

    public PollServiceTest() {
        openMocks(this);
        this.pollService = new PollService(
                pollRepository,
                memberRepository,
                teamRepository,
                teamMemberRepository,
                pollItemRepository
        );
    }

    @Test
    void 투표를_생성한다() {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest("title", 1, false, LocalDateTime.now().plusDays(1),
                List.of("item1", "item2"));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(teamRepository.findByCode(teamCode)).willReturn(Optional.of(new Team(1L, "team", "ABCD")));
        given(pollRepository.save(any(Poll.class)))
                .willReturn(new Poll(1L, null, null, null, null, null, null, null, null));

        // when
        Long pollId = pollService.createPoll(teamCode, memberId, pollCreateRequest);

        // then
        verify(pollItemRepository).saveAll(any());
        assertThat(pollId).isEqualTo(1L);
    }

    @Test
    void 투표_목록을_조회한다() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findAllByTeamId(anyLong()))
                .willReturn(List.of(new Poll(
                                1L,
                                null,
                                member,
                                null,
                                null,
                                null,
                                OPEN,
                                null,
                                null)
                        )
                );

        // when
        List<PollResponse> polls = pollService.findPolls(teamCode, memberId);

        // then
        Assertions.assertAll(
                () -> assertThat(polls).allMatch(response -> response.getStatus().equals("OPEN")),
                () -> assertThat(polls).allMatch(PollResponse::getIsHost)
        );
    }

    @Test
    void 투표를_진행한다() {
        // given
        PollItem pollItem1 = new PollItem(1L, null, "sub1");
        PollItem pollItem2 = new PollItem(2L, null, "sub2");

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong())).willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        member,
                        null,
                        2,
                        null,
                        OPEN,
                        null,
                        null,
                        List.of(pollItem1, pollItem2)
                )
        ));
        given(pollItemRepository.findById(1L)).willReturn(Optional.of(pollItem1));
        given(pollItemRepository.findById(2L)).willReturn(Optional.of(pollItem2));

        // when
        pollService.doPoll(teamCode, memberId, 1L, List.of(new PollItemRequest(1L, "그냥뇨"), new PollItemRequest(2L, "")));

        // then
        Assertions.assertAll(
                () -> assertThat(pollItem1.getPollResults()).hasSize(1),
                () -> assertThat(pollItem2.getPollResults()).hasSize(1)
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        PollResult pollResult1 = new PollResult(1L, null, member, "거의_다_한_것_같아요");
        PollResult pollResult2 = new PollResult(2L, null, member, "집에_가고_싶어요!");
        PollItem pollItem1 = new PollItem(1L, null, "sub1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, null, "sub2", new ArrayList<>(List.of(pollResult2)));
        PollItem pollItem3 = new PollItem(3L, null, "sub3");

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong())).willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        member,
                        null,
                        2,
                        null,
                        OPEN,
                        null,
                        null,
                        Arrays.asList(pollItem1, pollItem2, pollItem3)
                )
        ));

        given(pollItemRepository.findById(1L)).willReturn(Optional.of(pollItem1));
        given(pollItemRepository.findById(2L)).willReturn(Optional.of(pollItem2));
        given(pollItemRepository.findById(3L)).willReturn(Optional.of(pollItem3));

        // when
        pollService.doPoll(teamCode, memberId, 1L, List.of(new PollItemRequest(2L, "그냥뇨"), new PollItemRequest(3L, "")));

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
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                                1L,
                                new Team(1L, null, null),
                                member,
                                "test-poll-title",
                                null,
                                null,
                                CLOSED,
                                null,
                                null)
                        )
                );

        // when
        PollResponse poll = pollService.findPoll(teamCode,memberId, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(poll.getTitle()).isEqualTo("test-poll-title"),
                () -> assertThat(poll.getIsHost()).isTrue()
        );
    }

    @Test
    void 투표_선택_항목을_조회한다() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        member,
                        null,
                        null,
                        null,
                        CLOSED,
                        null,
                        null,
                        List.of(new PollItem(1L, null, "항목1"), new PollItem(2L, null, "항목2"))))
                );

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(teamCode,memberId, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1"),
                () -> assertThat(pollItemResponses.get(0).getSelected()).isFalse(),
                () -> assertThat(pollItemResponses.get(0).getDescription()).isBlank()
        );
    }

    @Test
    void 투표를_진행한_상태에서_투표_선택_항목을_조회한다() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        member,
                        null,
                        null,
                        null,
                        CLOSED,
                        null,
                        null,
                        List.of(new PollItem(1L, null, "항목1",
                                        List.of(new PollResult(null, null, member, "그냥뇨~"))),
                                new PollItem(2L, null, "항목2"))))
                );

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(teamCode,memberId, 1L);

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
        PollResult pollResult1 = new PollResult(1L, null, member, "거의_다_한_것_같아요");
        PollResult pollResult2 = new PollResult(2L, null, member, "집에_가고_싶어요!");

        Poll poll = new Poll(
                1L,
                null,
                member,
                null,
                null,
                true,
                CLOSED,
                null,
                null);
        PollItem pollItem1 = new PollItem(1L, poll, "항목1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, poll, "항목2", new ArrayList<>(List.of(pollResult2)));
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(poll));

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(teamCode,memberId, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(pollItemResultResponses).hasSize(2),
                () -> assertThat(pollItemResultResponses.get(0).getCount()).isEqualTo(1),
                () -> assertThat(pollItemResultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getId()).isEqualTo(0L),
                () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getName()).isBlank(),
                () -> assertThat(pollItemResultResponses.get(0).getMembers().get(0).getDescription()).isEqualTo(
                        "거의_다_한_것_같아요")
        );
    }

    @Test
    void 기명_투표_결과를_조회한다() {
        // given
        PollResult pollResult1 = new PollResult(1L, null, member, "거의_다_한_것_같아요");
        PollResult pollResult2 = new PollResult(2L, null, member, "집에_가고_싶어요!");

        Poll poll = new Poll(
                1L,
                null,
                member,
                null,
                null,
                false,
                CLOSED,
                null,
                null);
        PollItem pollItem1 = new PollItem(1L, poll, "항목1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, poll, "항목2", new ArrayList<>(List.of(pollResult2)));
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(poll)
                );

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(teamCode,memberId, 1L);

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
        given(memberRepository.findById(anyLong())).willReturn(
                Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));
        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                                1L,
                                new Team(1L, null, null),
                                member,
                                "test-poll-title",
                                null,
                                null,
                                CLOSED,
                                null,
                                null)
                        )
                );

        // when
        pollService.deletePoll(teamCode,memberId, 1L);

        // then
        verify(pollRepository).deleteById(1L);
    }

    @Test
    void 삭제_시_호스트가_아니면_예외를_던진다() {
        // given
        Member notHostMember = new Member(2L, "87654321", "eden", "eden-profile.cloudfront.net");
        given(memberRepository.findById(anyLong())).willReturn(
                Optional.of(notHostMember));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                                1L,
                                new Team(1L, null, null),
                                member,
                                "test-poll-title",
                                null,
                                null,
                                CLOSED,
                                null,
                                null)
                        )
                );

        // when & then
        assertThatThrownBy(() -> pollService.deletePoll(teamCode, 2L, 1L))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 투표를_종료한다() {
        // given
        Poll poll = new Poll(
                1L,
                new Team(1L, null, null),
                member,
                "test-poll-title",
                null,
                null,
                OPEN,
                null,
                null);
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(teamRepository.findIdByCode(teamCode)).willReturn(Optional.of(1L));

        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(poll));

        // when
        pollService.closePoll(teamCode  ,memberId, 1L);

        // then
        assertThat(poll.getStatus()).isEqualTo(CLOSED);
    }
}
