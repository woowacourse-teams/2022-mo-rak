package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.Team;

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
        member = new Member(1L, "ellie@naver.com", "ellie");

        poll = new Poll(1L, team, member, "title", 2, true, PollStatus.OPEN, LocalDateTime.now().plusDays(1),
            "ABCE",
            new ArrayList<>());
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
        // when
        poll.doPoll(List.of(itemB, itemC), member);
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
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> poll.doPoll(List.of(itemA, itemB, itemC), member));
    }

    @DisplayName("응답 개수가 0 인 경우 예외를 던진다.")
    @Test
    void validateAllowedPollCountsWithZero() {
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> poll.doPoll(List.of(), member));
    }

    @DisplayName("포함되지 않은 선택항목을 투표하는 경우 예외를 던진다.")
    @Test
    void validatePollItemBelongsTo() {
        // given
        // when
        PollItem itemD = new PollItem(4L, poll, "sub4", new ArrayList<>());
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> poll.doPoll(List.of(itemA, itemD), member));
    }
}