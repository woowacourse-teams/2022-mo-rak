package com.morak.back.brandnew;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.brandnew.domain.NewPollItem;
import org.junit.jupiter.api.Test;

class PollItemTest {

    @Test
    void poll_item_생성_시_select_members는_항상_초기화된다() {
        // when
        NewPollItem pollItem = NewPollItem.builder()
                .subject("볼링")
                .build();

        // then
        assertThat(pollItem.getSelectMembers()).isNotNull();
    }
}
