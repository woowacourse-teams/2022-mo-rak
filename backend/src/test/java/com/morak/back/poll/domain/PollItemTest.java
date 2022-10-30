package com.morak.back.poll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PollItemTest {

    @Test
    void poll_item_생성_시_select_members는_항상_초기화된다() {
        // when
        PollItem pollItem = PollItem.builder()
                .subject("볼링")
                .build();

        // then
        assertThat(pollItem.getSelectMembers()).isNotNull();
    }
}
