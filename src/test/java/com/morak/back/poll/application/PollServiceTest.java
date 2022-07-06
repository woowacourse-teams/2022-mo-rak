package com.morak.back.poll.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.*;

import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollResult;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResponse;

class PollServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollItemRepository pollItemRepository;

    private final PollService pollService;
    private Long tempMemberId = 1L;
    private Long tempTeamId = 1L;

    public PollServiceTest() {
        openMocks(this);
        this.pollService = new PollService(pollRepository, memberRepository, teamRepository, pollItemRepository);
    }

    @DisplayName("투표를 생성한다.")
    @Test
    void createPoll() {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest("title", 1, false, LocalDateTime.now().plusDays(1),
                List.of("item1", "item2"));
        given(memberRepository.findById(tempMemberId)).willReturn(Optional.of(new Member()));
        given(teamRepository.findById(tempTeamId)).willReturn(Optional.of(new Team()));
        given(pollRepository.save(any(Poll.class)))
                .willReturn(new Poll(1L, null, null, null, null, null, null, null, null));

        // when
        Long pollId = pollService.createPoll(tempTeamId, tempMemberId, pollCreateRequest);

        // then
        verify(pollItemRepository).saveAll(any());
        assertThat(pollId).isEqualTo(1L);
    }

    @DisplayName("투표 목록을 조회한다.")
    @Test
    void findPolls() {
        // given
        given(memberRepository.getById(anyLong())).willReturn(new Member(1L, "test-mail@email.com", "test-name"));
        given(pollRepository.findAllByTeamIdAndHostId(anyLong(), anyLong()))
                .willReturn(List.of(new Poll(
                                1L,
                                null,
                                new Member(1L, "test-mail@email.com", "test-name"),
                                null,
                                null,
                                null,
                                PollStatus.CLOSED,
                                null,
                                null)
                        )
                );
        // when
        List<PollResponse> polls = pollService.findPolls(1L, 1L);
        // then
        Assertions.assertAll(
                () -> assertThat(polls).allMatch(response -> response.getStatus().equals("CLOSED")),
                () -> assertThat(polls).allMatch(PollResponse::getIsHost)
        );
    }

    @DisplayName("투표를 진행한다.")
    @Test
    void doPoll() {
        // given
        Member member = new Member(1L, "test-mail@email.com", "test-name");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        PollItem pollItem1 = new PollItem(1L, null, "sub1");
        PollItem pollItem2 = new PollItem(2L, null, "sub2");
        given(pollRepository.findById(anyLong())).willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        new Member(1L, "test-mail@email.com", "test-name"),
                        null,
                        2,
                        null,
                        PollStatus.OPEN,
                        null,
                        null,
                        List.of(pollItem1, pollItem2)
                )
        ));
        given(pollItemRepository.findAllById(any())).willReturn(List.of(pollItem1, pollItem2));
        // when
        pollService.doPoll(1L, 1L, List.of(1L, 2L));
        // then
        Assertions.assertAll(
                () -> assertThat(pollItem1.getPollResults()).hasSize(1),
                () -> assertThat(pollItem2.getPollResults()).hasSize(1)
        );
    }

    @DisplayName("재투표를 진행한다.")
    @Test
    void rePoll() {
        // given
        Member member = new Member(1L, "test-mail@email.com", "test-name");
        PollResult pollResult1 = new PollResult(1L, null, member);
        PollResult pollResult2 = new PollResult(2L, null, member);
        PollItem pollItem1 = new PollItem(1L, null, "sub1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, null, "sub2", new ArrayList<>(List.of(pollResult2)));
        PollItem pollItem3 = new PollItem(3L, null, "sub3");

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(pollRepository.findById(anyLong())).willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        new Member(1L, "test-mail@email.com", "test-name"),
                        null,
                        2,
                        null,
                        PollStatus.OPEN,
                        null,
                        null,
                        Arrays.asList(pollItem1, pollItem2, pollItem3)
                )
        ));
        given(pollItemRepository.findAllById(any())).willReturn(Arrays.asList(pollItem2, pollItem3));
        // when
        pollService.doPoll(1L, 1L, Arrays.asList(2L, 3L));
        // then
        Assertions.assertAll(
                () -> assertThat(pollItem1.getPollResults()).hasSize(0),
                () -> assertThat(pollItem2.getPollResults()).hasSize(1),
                () -> assertThat(pollItem3.getPollResults()).hasSize(1)
        );
    }

    @DisplayName("투표 단건을 조회한다.")
    @Test
    void findPoll() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(
                Optional.of(new Member(1L, "test-mail@email.com", "test-name")));
        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                                1L,
                                new Team(1L, null, null),
                                new Member(1L, "test-mail@email.com", "test-name"),
                                "test-poll-title",
                                null,
                                null,
                                PollStatus.CLOSED,
                                null,
                                null)
                        )
                );
        // when
        PollResponse poll = pollService.findPoll(1L, 1L, 1L);
        // then
        Assertions.assertAll(
                () -> assertThat(poll.getTitle()).isEqualTo("test-poll-title"),
                () -> assertThat(poll.getIsHost()).isTrue()
        );
    }

    @DisplayName("투표 선택 항목을 조회한다.")
    @Test
    void findPollItems() {
        // given
        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(new Poll(
                        1L,
                        null,
                        new Member(1L, "test-mail@email.com", "test-name"),
                        null,
                        null,
                        null,
                        PollStatus.CLOSED,
                        null,
                        null,
                        List.of(new PollItem(1L, null, "항목1"), new PollItem(2L, null, "항목2"))))
                );

        // when
        List<PollItemResponse> pollItemResponses = pollService.findPollItems(1L, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(pollItemResponses).hasSize(2),
                () -> assertThat(pollItemResponses.get(0).getSubject()).isEqualTo("항목1")
        );
    }

    @DisplayName("익명 투표 결과를 조회한다.")
    @Test
    void findPollResultsWithAnonymous() {
        // given
        Member member = new Member(1L, "test-mail@email.com", "test-name");

        PollResult pollResult1 = new PollResult(1L, null, member);
        PollResult pollResult2 = new PollResult(2L, null, member);


        Poll poll = new Poll(
                1L,
                null,
                new Member(1L, "test-mail@email.com", "test-name"),
                null,
                null,
                true,
                PollStatus.CLOSED,
                null,
                null);
        PollItem pollItem1 = new PollItem(1L, poll, "항목1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, poll, "항목2", new ArrayList<>(List.of(pollResult2)));
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);
        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(poll)
                );

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(1L, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(pollItemResultResponses).hasSize(2),
                () -> assertThat(pollItemResultResponses.get(0).getMembers()).hasSize(0),
                () -> assertThat(pollItemResultResponses.get(0).getCount()).isEqualTo(1)
        );
    }

    @DisplayName("무기명 투표 결과를 조회한다.")
    @Test
    void findPollResultsWithNotAnonymous() {
        // given
        Member member = new Member(1L, "test-mail@email.com", "test-name");

        PollResult pollResult1 = new PollResult(1L, null, member);
        PollResult pollResult2 = new PollResult(2L, null, member);


        Poll poll = new Poll(
                1L,
                null,
                new Member(1L, "test-mail@email.com", "test-name"),
                null,
                null,
                false,
                PollStatus.CLOSED,
                null,
                null);
        PollItem pollItem1 = new PollItem(1L, poll, "항목1", new ArrayList<>(List.of(pollResult1)));
        PollItem pollItem2 = new PollItem(2L, poll, "항목2", new ArrayList<>(List.of(pollResult2)));
        poll.addItem(pollItem1);
        poll.addItem(pollItem2);
        given(pollRepository.findByIdAndTeamId(anyLong(), anyLong()))
                .willReturn(Optional.of(poll)
                );

        // when
        List<PollItemResultResponse> pollItemResultResponses = pollService.findPollItemResults(1L, 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(pollItemResultResponses).hasSize(2),
                () -> assertThat(pollItemResultResponses.get(0).getMembers()).hasSize(1),
                () -> assertThat(pollItemResultResponses.get(0).getCount()).isEqualTo(1)
        );
    }
}