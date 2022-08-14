package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DateTimePeriodTest {

    @Test
    void 생성시_마지막_시점이_시작_시점보다_과거일_경우_예외를_던진다() {
        LocalDateTime startDateTime = LocalDateTime.now().withNano(0).plusDays(2);
        LocalDateTime endDateTime = LocalDateTime.now().withNano(0).plusDays(1);
        int minutesUnit = 30;
        // when & then

        assertThatThrownBy(() -> DateTimePeriod.of(startDateTime, endDateTime, minutesUnit))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR);

    }

    @ParameterizedTest
    @ValueSource(ints = {29, 31})
    void 약속잡기_가능_시각이_30분으로_나눠지지_않으면_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> DateTimePeriod.of(LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0).plusMinutes(minutes), 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 40",
            "20, 50"
    })
    void 약속잡기_가능_시간이_30분이지만_0분_또는_30분이_아닌경우_예외를_던진다(int startMinutes, int endMinutes) {
        // when & then
        assertThatThrownBy(() -> DateTimePeriod.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, startMinutes)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(16, endMinutes)), 30))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR);
    }
}
