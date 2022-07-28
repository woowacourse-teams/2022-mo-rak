package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

class AppointmentStatusTest {

    @Test
    void 약속잡기가_종료된_경우_true를_반환한다() {
        // given
        AppointmentStatus closed = AppointmentStatus.CLOSED;

        // when & then
        assertThat(closed.isClosed()).isTrue();
    }

    @Test
    void 약속잡기가_종료되지_않은_경우_false를_반환한다() {
        // given
        AppointmentStatus open = AppointmentStatus.OPEN;

        // when & then
        assertThat(open.isClosed()).isFalse();
    }

    @Test
    void 상태를_close로_만든다() {
        //given
        AppointmentStatus open = AppointmentStatus.OPEN;

        //when
        AppointmentStatus status = open.close();

        //then
        assertThat(status).isEqualTo(AppointmentStatus.CLOSED);
    }

    @Test
    void 상태가_close일떄_close_하는_경우_예외를_던진다() {
        //given
        AppointmentStatus status = AppointmentStatus.CLOSED;

        //when & then
        assertThatThrownBy(() -> status.close())
                .isInstanceOf(InvalidRequestException.class);
    }
}
