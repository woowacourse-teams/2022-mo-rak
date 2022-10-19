package com.morak.back.appointment.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class MenuStatusTest {

    @Test
    void 종료된_상태인_경우_true를_반환한다() {
        // given
        MenuStatus closed = MenuStatus.CLOSED;

        // when & then
        assertThat(closed.isClosed()).isTrue();
    }

    @Test
    void 종료되지_않은_상태인_경우_false를_반환한다() {
        // given
        MenuStatus open = MenuStatus.OPEN;

        // when & then
        assertThat(open.isClosed()).isFalse();
    }

    @Test
    void 상태를_close로_만든다() {
        // given
        MenuStatus open = MenuStatus.OPEN;

        // when
        MenuStatus status = open.close();

        // then
        assertThat(status).isEqualTo(MenuStatus.CLOSED);
    }

    @Test
    void 상태가_close일_때_close_하는_경우_예외를_던진다() {
        // given
        MenuStatus status = MenuStatus.CLOSED;

        // when & then
        assertThatThrownBy(status::close)
                .isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MENU_ALREADY_CLOSED_ERROR);
    }
}
