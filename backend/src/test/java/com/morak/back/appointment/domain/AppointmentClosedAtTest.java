package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.exception.DomainLogicException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class AppointmentClosedAtTest {

    @Test
    void 마감시각이_종료시각보다_이후일_때_예외를_던진다() {
        // given
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalTime endTime = LocalTime.now().plusMinutes(1);
        LocalDateTime closedAtDateTime = LocalDateTime.now().plusDays(10).plusMinutes(1);

        // when & then
        assertThatThrownBy(
                () -> new AppointmentClosedAt(closedAtDateTime, LocalDateTime.now(), endDate, endTime)
        ).isInstanceOf(DomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }
}
