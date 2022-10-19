package com.morak.back.poll.domain;

import static com.morak.back.poll.AuthFixture.createMember;
import static com.morak.back.poll.AuthFixture.createTeam;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_00;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_30;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
import com.morak.back.poll.exception.PollItemNotFoundException;
import com.morak.back.team.domain.Team;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollTest {

    private final Team team = createTeam(1L, "모락", "12345678");
    private final Member member = createMember(1L, "엘리");
    private final Member otherMember = createMember(2L, "에덴");

    private Poll poll;
    private PollItem itemA;
    private PollItem itemB;
    private PollItem itemC;
    private String descriptionA = "삼겹살이 젤루 맛남!";
    private String descriptionB = "꼬소한 회가 좋아!";
    private String descriptionC = "우리팀 사람들이 좋아함!";

    @BeforeEach
    void setUp() {
        PollInfo info = PollInfo.builder()
                .codeGenerator(l -> "12345678")
                .title("모락 회식 메뉴")
                .anonymous(false)
                .allowedCount(2)
                .teamId(team.getId())
                .hostId(member.getId())
                .status(PollStatus.OPEN)
                .closedAt(SystemDateTime.builder()
                        .dateTime(TIME_OF_2022_05_12_12_30)
                        .now(TIME_OF_2022_05_12_12_00)
                        .build())
                .build();
        itemA = PollItem.builder()
                .id(1L)
                .subject("삼겹살")
                .build();
        itemB = PollItem.builder()
                .id(2L)
                .subject("회")
                .build();
        itemC = PollItem.builder()
                .id(3L)
                .subject("이자카야")
                .build();
        poll = Poll.builder()
                .pollInfo(info)
                .pollItems(List.of(itemA, itemB, itemC))
                .build();
    }

    @Test
    void 투표_생성시_마감_시간이_현재보다_이전이면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> PollInfo.builder()
                .codeGenerator(l -> "12345678")
                .title("모락 회식 메뉴")
                .anonymous(false)
                .allowedCount(2)
                .teamId(team.getId())
                .hostId(member.getId())
                .status(PollStatus.OPEN)
                .closedAt(SystemDateTime.builder()
                        .dateTime(TIME_OF_2022_05_12_12_00)
                        .now(TIME_OF_2022_05_12_12_30)
                        .build())
                .build())
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 투표_생성시_투표_항목_개수가_허용_개수보다_적으면_예외를_던진다() {
        // given
        PollInfo info = PollInfo.builder()
                .codeGenerator(l -> "12345678")
                .title("모락 회식 메뉴")
                .anonymous(false)
                .allowedCount(4)
                .teamId(team.getId())
                .hostId(member.getId())
                .status(PollStatus.OPEN)
                .closedAt(SystemDateTime.builder()
                        .dateTime(TIME_OF_2022_05_12_12_30)
                        .now(TIME_OF_2022_05_12_12_00)
                        .build())
                .build();

        // when & then
        assertThatThrownBy(() -> poll = Poll.builder()
                .pollInfo(info)
                .pollItems(List.of(itemA, itemB, itemC))
                .build())
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 투표를_진행한다() {
        // given
        Map<PollItem, String> selectData = new HashMap<>();
        selectData.put(itemA, descriptionA);
        selectData.put(itemC, descriptionC);

        // when
        poll.doPoll(member, selectData);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers().getValues()).containsExactly(entry(member, descriptionA)),
                () -> assertThat(itemB.getSelectMembers().getValues()).hasSize(0),
                () -> assertThat(itemC.getSelectMembers().getValues()).containsExactly(entry(member, descriptionC))
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        Map<PollItem, String> selectData1 = new HashMap<>();
        selectData1.put(itemA, descriptionA);
        selectData1.put(itemC, descriptionC);
        poll.doPoll(member, selectData1);

        // when
        Map<PollItem, String> selectData2 = new HashMap<>();
        selectData2.put(itemB, descriptionB);
        poll.doPoll(member, selectData2);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers().getValues()).hasSize(0),
                () -> assertThat(itemB.getSelectMembers().getValues()).containsExactly(entry(member, descriptionB)),
                () -> assertThat(itemC.getSelectMembers().getValues()).hasSize(0)
        );
    }

    @Test
    void 여러명의_멤버가_투표를_진행한다() {
        // given
        Map<PollItem, String> selectData1 = new HashMap<>();
        selectData1.put(itemA, descriptionA);
        selectData1.put(itemC, descriptionC);
        poll.doPoll(member, selectData1);

        // when
        Map<PollItem, String> selectData2 = new HashMap<>();
        selectData2.put(itemA, descriptionA);
        selectData2.put(itemB, descriptionB);
        poll.doPoll(otherMember, selectData2);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers().getValues()).containsOnly(entry(member, descriptionA),
                        entry(otherMember, descriptionA)),
                () -> assertThat(itemB.getSelectMembers().getValues()).containsExactly(
                        entry(otherMember, descriptionB)),
                () -> assertThat(itemC.getSelectMembers().getValues()).containsExactly(entry(member, descriptionC))
        );
    }

    @Test
    void 투표하는_선택_항목의_개수가_허용_개수보다_많으면_예외를_던진다() {
        // given
        Map<PollItem, String> selectData = new HashMap<>();
        selectData.put(itemA, descriptionA);
        selectData.put(itemB, descriptionB);
        selectData.put(itemC, descriptionC);

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, selectData))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 투표하는_선택_항목의_개수가_0개인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, new HashMap<>()))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 호스트가_투표를_종료한다() {
        // when
        poll.close(member.getId());

        // then
        assertThat(poll.getPollInfo().getStatus()).isEqualTo(PollStatus.CLOSED);
    }

    @Test
    void 호스트가_아닌_멤버가_투표를_종료하는_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll.close(otherMember.getId()))
                .isInstanceOf(PollAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_HOST_MISMATCHED_ERROR);
    }

    @Test
    void 종료된_투표를_종료하는_경우_예외를_던진다() {
        // given
        poll.close(member.getId());

        // when & then
        assertThatThrownBy(() -> poll.close(member.getId()))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 종료된_투표에_투표를_진행하려고_하면_예외를_던진다() {
        // given
        poll.close(member.getId());

        Map<PollItem, String> selectData = new HashMap<>();
        selectData.put(itemA, descriptionA);
        selectData.put(itemC, descriptionC);

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, selectData))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 투표에_속하지_않은_선택항목에_투표하려는_경우_예외를_던진다() {
        // given
        PollItem itemD = PollItem.builder()
                .id(4L)
                .subject("서브웨이")
                .build();

        Map<PollItem, String> selectData = new HashMap<>();
        selectData.put(itemC, descriptionC);
        selectData.put(itemD, "");

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member, selectData))
                .isInstanceOf(PollItemNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR);
    }
}
