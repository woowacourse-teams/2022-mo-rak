package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DateTimePeriodTest {

    @Test
    void 마지막_시점이_시작_시점보다_과거일_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> DateTimePeriod.of(LocalDateTime.now().withNano(0).plusDays(2),
                LocalDateTime.now().withNano(0).plusDays(1), 30)
        )
                .isInstanceOf(AppointmentDomainLogicException.class);

    }

    @ParameterizedTest
    @ValueSource(ints = {29, 31})
    void 약속잡기_가능_시간이_30분이_아닌_경우_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> DateTimePeriod.of(LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0).plusMinutes(minutes), 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 40",
            "20, 50"
    })
    void 약속잡기_가능_시간이_30분이_아닌_경우_예외를_던진다(int startMinutes, int endMinutes) {
        // when & then
        assertThatThrownBy(() -> DateTimePeriod.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, startMinutes)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(16, endMinutes)), 30))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }
}
