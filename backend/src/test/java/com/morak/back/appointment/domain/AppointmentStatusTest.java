package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}
