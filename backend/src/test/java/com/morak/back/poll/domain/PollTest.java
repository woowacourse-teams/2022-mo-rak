package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
        team = Team.builder()
                .id(1L)
                .name("team1")
                .code(Code.generate(length -> "abcd1234")).build();
        member = Member.builder()
                .id(1L)
                .oauthId("12345678")
                .name("ellie")
                .profileUrl("http://ellie-profile.com")
                .build();
        poll = Poll.builder()
                .id(1L)
                .team(team)
                .host(member)
                .title("title")
                .allowedPollCount(2)
                .isAnonymous(true)
                .status(PollStatus.OPEN)
                .closedAt(LocalDateTime.now().plusDays(1L))
                .code(Code.generate(length -> "ABCD1234"))
                .build();
        itemA = PollItem.builder()
                .id(1L)
                .poll(poll)
                .subject("sub1")
                .build();
        itemB = PollItem.builder()
                .id(2L)
                .poll(poll)
                .subject("sub2")
                .build();
        itemC = PollItem.builder()
                .id(3L)
                .poll(poll)
                .subject("sub3")
                .build();
        poll.addItem(itemA);
        poll.addItem(itemB);
        poll.addItem(itemC);
    }

    @Test
    void 투표를_진행한다() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        String description1 = "거의_다_왔어요!";
        String description2 = "힘내!";
        mappedItemAndDescription.put(itemB, description1);
        mappedItemAndDescription.put(itemC, description2);

        // when
        poll.doPoll(member, mappedItemAndDescription);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getPollResults()).hasSize(0),
                () -> assertThat(itemB.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description1)),
                () -> assertThat(itemC.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description2))
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemB, "거의_다왔어요!");
        mappedItemAndDescription.put(itemC, "힘내!");
        poll.doPoll(member, mappedItemAndDescription);

        Map<PollItem, String> reMappedItemAndDescription = new HashMap<>();
        String description1 = "화장실_다녀오세요.";
        String description2 = "거의_다왔어요!";
        reMappedItemAndDescription.put(itemA, description1);
        reMappedItemAndDescription.put(itemB, description2);

        // when
        poll.doPoll(member, reMappedItemAndDescription);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description1)),
                () -> assertThat(itemB.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description2)),
                () -> assertThat(itemC.getPollResults()).hasSize(0)
        );
    }

    @Test
    void 여러명의_멤버가_투표를_진행한다() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        String description1 = "거의_다왔어요!";
        String description2 = "힘내!";
        mappedItemAndDescription.put(itemB, description1);
        mappedItemAndDescription.put(itemC, description2);
        poll.doPoll(member, mappedItemAndDescription);


        Member otherMember = Member.builder()
                .id(2L)
                .oauthId("otherMem")
                .name("other")
                .profileUrl("http://seongwoo-profile.com")
                .build();
        Map<PollItem, String> reMappedItemAndDescription = new HashMap<>();
        String description3 = "화장실_다녀오세요.";
        String description4 = "거의_다왔어요!";
        reMappedItemAndDescription.put(itemA, description3);
        reMappedItemAndDescription.put(itemB, description4);

        // when
        poll.doPoll(otherMember, reMappedItemAndDescription);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(otherMember, description3)),
                () -> assertThat(itemB.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description1), tuple(otherMember, description4)),
                () -> assertThat(itemC.getPollResults()).extracting("member", "description")
                        .containsExactly(tuple(member, description2))
        );
    }

    @Test
    void 가능한_복수_응답_개수를_초과하는_경우_예외를_던진다() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemA, "화장실_다녀오세요.");
        mappedItemAndDescription.put(itemB, "거의_다왔어요!");
        mappedItemAndDescription.put(itemC, "힘내!!");

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, mappedItemAndDescription))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 응답_개수가_0인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, new HashMap<>()))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 호스트가_투표를_종료한다() {
        // when
        poll.close(member);

        // then
        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED);
    }

    @Test
    void 호스트가_아닌_멤버가_투표를_종료하는_경우_예외를_던진다() {
        // given
        Member member = Member.builder()
                .id(100L)
                .oauthId("174837283")
                .name("ohzzi")
                .profileUrl("http://wrong-member")
                .build();

        // when & then
        assertThatThrownBy(() -> poll.close(member))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
    }

    @Test
    void 이미_투표가_종료된_상태에서_다시_종료하는_경우_예외를_던진다() {
        // given
        poll.close(member);

        // then
        assertThatThrownBy(() -> poll.close(member))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 종료된_투표에_투표를_하면_예외를_던진다() {
        // given
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemA, "화장실_다녀오세요.");
        mappedItemAndDescription.put(itemB, "거의_다왔어요!");

        // when
        poll.close(member);

        // then
        assertThatThrownBy(() -> poll.doPoll(member, mappedItemAndDescription))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 투표에_속하지_않은_선택항목을_투표하는_경우_예외를_던진다() {
        // given
        PollItem itemD = PollItem.builder()
                .id(4L)
                .subject("sub4")
                .build();
        Map<PollItem, String> mappedItemAndDescription = new HashMap<>();
        mappedItemAndDescription.put(itemA, "빨강_프링글스는_별로야");
        mappedItemAndDescription.put(itemD, "프링글스는_초록_프링글스지");

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, mappedItemAndDescription))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_MISMATCHED_ERROR);
    }

    @Test
    void 멤버가_투표의_호스트면_true를_반환한다() {
        // when
        boolean isHost = poll.isHost(member);

        // then
        assertThat(isHost).isTrue();
    }

    @Test
    void 멤버가_투표의_호스트가_아니면_false를_반환한다() {
        // given
        Member member = Member.builder()
                .id(3L)
                .oauthId("13579246")
                .name("bkr")
                .profileUrl("http://bkr-profile.com")
                .build();

        // when
        boolean isHost = poll.isHost(member);

        // then
        assertThat(isHost).isFalse();
    }
}
