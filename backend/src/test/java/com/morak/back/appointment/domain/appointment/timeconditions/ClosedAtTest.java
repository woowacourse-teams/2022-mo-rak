package com.morak.back.appointment.domain.appointment.timeconditions;

import static com.morak.back.core.exception.CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ClosedAtTest {

    @Test
    void closedAt_정상_생성() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime closedAtTime = now.plusMinutes(1);
        LocalDateTime endDateTime = now.plusDays(3);

        // when
        ClosedAt closedAt = ClosedAt.of(closedAtTime, endDateTime, now);

        // then
        assertThat(closedAt.getClosedAt()).isEqualTo(closedAtTime);
    }

    @Test
    void 종료_시간이_현재_시간보다_이전_예외를_던진다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime closedAtTime = now.minusDays(1);
        LocalDateTime endDateTime = now.plusDays(3);

        // when & then
        assertThatThrownBy(() -> ClosedAt.of(closedAtTime, endDateTime, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }

    @Test
    void closedAtTime이_선택_끝_시간보다_나중이면_예외를_던진다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime closedAtTime = now.plusDays(4);
        LocalDateTime endDateTime = now.plusDays(3);

        // when & then
        assertThatThrownBy(() -> ClosedAt.of(closedAtTime, endDateTime, now))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR);
    }
}
