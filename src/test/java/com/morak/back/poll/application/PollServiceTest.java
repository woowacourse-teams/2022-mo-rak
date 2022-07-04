package com.morak.back.poll.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.time.LocalDateTime;
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
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
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
}