package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollDomainLogicException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PollItemsTest {

    @Test
    void 투표_항목이_0개이면_예외를_던진다() {
        // given
        List<PollItem> pollItems = new ArrayList<>();
        AllowedCount allowedCount = new AllowedCount(1);

        // when & then
        assertThatThrownBy(() -> PollItems.builder().values(pollItems).allowedCount(allowedCount).build())
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_COUNT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void 투표_항목이_선택_가능_개수보다_적으면_예외를_던진다() {
        // given
        List<PollItem> pollItems = List.of(
                PollItem.builder().subject("자바기").build(),
                PollItem.builder().subject("눈을감자").build()
        );
        AllowedCount allowedCount = new AllowedCount(3);

        // when & then
        assertThatThrownBy(() -> PollItems.builder().values(pollItems).allowedCount(allowedCount).build())
                .isInstanceOf(PollDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.POLL_ITEM_COUNT_OUT_OF_RANGE_ERROR);
    }
}
