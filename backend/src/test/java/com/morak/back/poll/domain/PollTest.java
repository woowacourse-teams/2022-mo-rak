package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.Team;
import com.morak.back.poll.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PollTest {

    private Team team;
    private Member member;
    private Poll poll;
    private PollItem itemA;
    private PollItem itemB;
    private PollItem itemC;

    @BeforeEach
    void setUp() {
        team = new Team(1L, "team1", "TEAM");
        member = new Member(1L, "12345678", "ellie", "ellie-profile.com");

        poll = new Poll(1L, team, member, "title", 2, true, PollStatus.OPEN, LocalDateTime.now().plusDays(1),
            "ABCE", new ArrayList<>());
        itemA = new PollItem(1L, poll, "sub1", new ArrayList<>());
        itemB = new PollItem(2L, poll, "sub2", new ArrayList<>());
        itemC = new PollItem(3L, poll, "sub3", new ArrayList<>());
        poll.addItem(itemA);
        poll.addItem(itemB);
        poll.addItem(itemC);
    }

    @DisplayName("투표를 진행한다.")
    @Test
    void doPoll() {
        // given
        List<PollItem> pollItems = List.of(this.itemB, itemC);

        // when
        poll.doPoll(pollItems, member);

        // then
        List<PollResult> pollResults = poll.getPollItems().get(1).getPollResults();
        Assertions.assertAll(
            () -> assertThat(pollResults).hasSize(1),
            () -> assertThat(pollResults.get(0).getMember()).isSameAs(member)
        );
    }

    @DisplayName("재투표를 진행한다.")
    @Test
    void rePoll() {
        // given
        poll.doPoll(List.of(itemB, itemC), member);

        // when
        poll.doPoll(List.of(itemA, itemB), member);

        // then
        List<PollResult> pollResults1 = poll.getPollItems().get(0).getPollResults();
        List<PollResult> pollResults2 = poll.getPollItems().get(1).getPollResults();
        List<PollResult> pollResults3 = poll.getPollItems().get(2).getPollResults();
        Assertions.assertAll(
            () -> assertThat(pollResults1.get(0).getMember()).isSameAs(member),
            () -> assertThat(pollResults2.get(0).getMember()).isSameAs(member),
            () -> assertThat(pollResults3).hasSize(0)
        );
    }

    @DisplayName("가능한 복수 응답 개수를 초과하는 경우 예외를 던진다.")
    @Test
    void validateAllowedPollCountsWithOverflow() {
        // then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> poll.doPoll(List.of(itemA, itemB, itemC), member));
    }

    @DisplayName("응답 개수가 0 인 경우 예외를 던진다.")
    @Test
    void validateAllowedPollCountsWithZero() {
        // then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> poll.doPoll(List.of(), member));
    }

    @DisplayName("포함되지 않은 선택항목을 투표하는 경우 예외를 던진다.")
    @Test
    void validatePollItemBelongsTo() {
        // given
        PollItem itemD = new PollItem(4L, poll, "sub4", new ArrayList<>());

        // when & then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> poll.doPoll(List.of(itemA, itemD), member));
    }

    @DisplayName("호스트가 아닐 시 예외를 던진다.")
    @Test
    public void validateHost() {
        // given
        Member member = new Member(3L, "13579246", "bkr", "bkr-profile.com");

        // when & then
        assertThatThrownBy(() -> poll.validateHost(member))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("호스트가 투표를 종료한다")
    @Test
    void closePoll() {
        // given & when
        poll.close(member);

        // then
        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED);
    }

    @DisplayName("이미 투표가 종료된 상태에서 다시 종료하는 경우 예외를 던진다.")
    @Test
    void throwsExceptionOnClosingPollTwice() {
        // given & when
        poll.close(member);

        // then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> poll.close(member));
    }

    @DisplayName("호스트가 아닌 멤버가 투표를 종료하는 경우 예외를 던진다.")
    @Test
    void validateHostWhenClosingPoll() {
        // given & when
        Member member = new Member(100L, "174837283", "ohzzi", "wrong-member");

        // then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> poll.close(member));
    }
}
