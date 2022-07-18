package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.Team;
import com.morak.back.poll.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemB, "거의 다 왔어요!");
        mappedItemAndDescription.put(itemC, "힘내!");

        // when
        poll.doPoll(member, mappedItemAndDescription);

        // then
        List<PollResult> pollResults = poll.getPollItems().get(1).getPollResults();
        Assertions.assertAll(
                () -> assertThat(pollResults).hasSize(1),
                () -> assertThat(pollResults.get(0).getMember()).isSameAs(member),
                () -> assertThat(pollResults.get(0).getDescription()).isEqualTo("거의 다 왔어요!")
        );
    }

    @DisplayName("재투표를 진행한다.")
    @Test
    void rePoll() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemB, "거의 다왔어요!");
        mappedItemAndDescription.put(itemC, "힘내!");
        poll.doPoll(member, mappedItemAndDescription);

        Map<PollItem, String> reMappedItemAndDescription = new HashMap<>();
        reMappedItemAndDescription.put(itemA, "화장실 다녀오세요.");
        reMappedItemAndDescription.put(itemB, "거의 다왔어요!");

        // when
        poll.doPoll(member, reMappedItemAndDescription);

        // then
        List<PollResult> pollResults1 = poll.getPollItems().get(0).getPollResults();
        List<PollResult> pollResults2 = poll.getPollItems().get(1).getPollResults();
        List<PollResult> pollResults3 = poll.getPollItems().get(2).getPollResults();
        Assertions.assertAll(
                () -> assertThat(pollResults1.get(0).getMember()).isSameAs(member),
                () -> assertThat(pollResults1.get(0).getDescription()).isEqualTo("화장실 다녀오세요."),
                () -> assertThat(pollResults2.get(0).getMember()).isSameAs(member),
                () -> assertThat(pollResults3).hasSize(0)
        );
    }

    @DisplayName("가능한 복수 응답 개수를 초과하는 경우 예외를 던진다.")
    @Test
    void validateAllowedPollCountsWithOverflow() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemA, "화장실 다녀오세요.");
        mappedItemAndDescription.put(itemB, "거의 다왔어요!");
        mappedItemAndDescription.put(itemC, "힘내!!");

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, mappedItemAndDescription))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("응답 개수가 0 인 경우 예외를 던진다.")
    @Test
    void validateAllowedPollCountsWithZero() {
        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, new HashMap<>()))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("투표에 속하지 않은 선택항목을 투표하는 경우 예외를 던진다.")
    @Test
    void validatePollItemBelongsTo() {
        // given
        PollItem itemD = new PollItem(4L, poll, "sub4", new ArrayList<>());
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemA, "빨강 프링글스는 별로야");
        mappedItemAndDescription.put(itemD, "프링글스는 초록 프링글스지");

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, mappedItemAndDescription))
                .isInstanceOf(InvalidRequestException.class);
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
        // when
        poll.close(member);

        // then
        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED);
    }

    @DisplayName("이미 투표가 종료된 상태에서 다시 종료하는 경우 예외를 던진다.")
    @Test
    void throwsExceptionOnClosingPollTwice() {
        // given
        poll.close(member);

        // then
        assertThatThrownBy(() -> poll.close(member))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("호스트가 아닌 멤버가 투표를 종료하는 경우 예외를 던진다.")
    @Test
    void validateHostWhenClosingPoll() {
        // given
        Member member = new Member(100L, "174837283", "ohzzi", "wrong-member");

        // when & then
        assertThatThrownBy(() -> poll.close(member))
                .isInstanceOf(InvalidRequestException.class);
    }
}
