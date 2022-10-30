package com.morak.back.poll.domain;

import static com.morak.back.poll.AuthFixture.createMember;
import static com.morak.back.poll.AuthFixture.createTeam;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_00;
import static com.morak.back.poll.DateTimeFixture.TIME_OF_2022_05_12_12_30;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.core.exception.AuthorizationException;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import com.morak.back.poll.domain.Poll.PollBuilder;
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
    private final Member member = createMember(1L, "ellieOAuthId", "엘리",
            "https://avatars.githubusercontent.com/u/79205414?v=4");
    private final Member otherMember = createMember(2L, "edenOAuthId", "에덴",
            "https://avatars.githubusercontent.com/u/79205414?v=4");
    private final String descriptionA = "삼겹살이 젤루 맛남!";
    private final String descriptionB = "꼬소한 회가 좋아!";
    private final String descriptionC = "우리팀 사람들이 좋아함!";

    private Poll poll;
    private PollItem itemA;
    private PollItem itemB;
    private PollItem itemC;

    @BeforeEach
    void setUp() {
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
        poll = defaultPollBuilder()
                .build();
    }

    @Test
    void 투표_생성시_마감_시간이_현재보다_이전이면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> defaultPollBuilder()
                .closedAt(new ClosedAt(TIME_OF_2022_05_12_12_00, TIME_OF_2022_05_12_12_30))
                .build())
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.PAST_CLOSED_TIME_ERROR);
    }

    @Test
    void 투표_생성시_투표_항목_개수가_허용_개수보다_적으면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll = defaultPollBuilder()
                .allowedCount(4)
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
        poll.doPoll(member.getId(), selectData);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers()).containsExactly(entry(member.getId(), descriptionA)),
                () -> assertThat(itemB.getSelectMembers()).hasSize(0),
                () -> assertThat(itemC.getSelectMembers()).containsExactly(entry(member.getId(), descriptionC))
        );
    }

    @Test
    void 재투표를_진행한다() {
        // given
        Map<PollItem, String> selectData1 = new HashMap<>();
        selectData1.put(itemA, descriptionA);
        selectData1.put(itemC, descriptionC);
        poll.doPoll(member.getId(), selectData1);

        // when
        Map<PollItem, String> selectData2 = new HashMap<>();
        selectData2.put(itemB, descriptionB);
        poll.doPoll(member.getId(), selectData2);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers()).isEmpty(),
                () -> assertThat(itemB.getSelectMembers()).containsExactly(entry(member.getId(), descriptionB)),
                () -> assertThat(itemC.getSelectMembers()).isEmpty()
        );
    }

    @Test
    void 여러명의_멤버가_투표를_진행한다() {
        // given
        Map<PollItem, String> selectData1 = new HashMap<>();
        selectData1.put(itemA, descriptionA);
        selectData1.put(itemC, descriptionC);
        poll.doPoll(member.getId(), selectData1);

        // when
        Map<PollItem, String> selectData2 = new HashMap<>();
        selectData2.put(itemA, descriptionA);
        selectData2.put(itemB, descriptionB);
        poll.doPoll(otherMember.getId(), selectData2);

        // then
        Assertions.assertAll(
                () -> assertThat(itemA.getSelectMembers()).containsOnly(entry(member.getId(), descriptionA),
                        entry(otherMember.getId(), descriptionA)),
                () -> assertThat(itemB.getSelectMembers()).containsExactly(
                        entry(otherMember.getId(), descriptionB)),
                () -> assertThat(itemC.getSelectMembers()).containsExactly(entry(member.getId(), descriptionC))
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
        assertThatThrownBy(() -> poll.doPoll(member.getId(), selectData))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 투표하는_선택_항목의_개수가_0개인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll.doPoll(member.getId(), new HashMap<>()))
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 호스트가_투표를_종료한다() {
        // when
        poll.close(member.getId());

        // then
        assertThat(poll.getStatus()).isEqualTo(PollStatus.CLOSED.name());
    }

    @Test
    void 호스트가_아닌_멤버가_투표를_종료하는_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> poll.close(otherMember.getId()))
                .isInstanceOf(AuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.HOST_MISMATCHED_ERROR);
    }

    @Test
    void 종료된_투표를_종료하는_경우_예외를_던진다() {
        // given
        poll.close(member.getId());

        // when & then
        assertThatThrownBy(() -> poll.close(member.getId()))
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MENU_ALREADY_CLOSED_ERROR);
    }

    @Test
    void 종료된_투표에_투표를_진행하려고_하면_예외를_던진다() {
        // given
        poll.close(member.getId());

        Map<PollItem, String> selectData = new HashMap<>();
        selectData.put(itemA, descriptionA);
        selectData.put(itemC, descriptionC);

        // when & then
        assertThatThrownBy(() -> poll.doPoll(member.getId(), selectData))
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
        assertThatThrownBy(() -> poll.doPoll(member.getId(), selectData))
                .isInstanceOf(PollItemNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_NOT_FOUND_ERROR);
    }

    private PollBuilder defaultPollBuilder() {
        return Poll.builder()
                .code(Code.generate(l -> "12345678"))
                .title("모락 회식 메뉴")
                .teamCode(Code.generate((s) -> team.getCode()))
                .hostId(member.getId())
                .status(MenuStatus.OPEN)
                .closedAt(new ClosedAt(TIME_OF_2022_05_12_12_30, TIME_OF_2022_05_12_12_00))
                .pollItems(List.of(itemA, itemB, itemC))
                .anonymous(false)
                .allowedCount(2);
    }
}
