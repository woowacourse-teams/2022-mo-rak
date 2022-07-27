package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AppointmentTest {

    @ParameterizedTest
    @CsvSource({
            "60, 1, 0",
            "150, 2, 30"
    })
    void 분에서_시간과_분을_추출한다(int durationMinutes, int hours, int minutes) {
        // given
        Appointment appointment = Appointment.builder()
                .durationMinutes(durationMinutes)
                .build();

        // when & then
        Assertions.assertAll(
                () -> assertThat(appointment.parseHours()).isEqualTo(hours),
                () -> assertThat(appointment.parseMinutes()).isEqualTo(minutes)
        );
    }

    @Test
    void 약속잡기가_종료된_경우_true를_반환한다() {
        // given
        Appointment appointment = Appointment.builder()
                .status(AppointmentStatus.CLOSED)
                .build();

        // when & then
        assertThat(appointment.isClosed()).isTrue();
    }

    @Test
    void 약속잡기가_종료되지_않은_경우_false를_반환한다() {
        // given
        Appointment appointment = Appointment.builder()
                .status(AppointmentStatus.OPEN)
                .build();

        // when & then
        assertThat(appointment.isClosed()).isFalse();
    }
}
