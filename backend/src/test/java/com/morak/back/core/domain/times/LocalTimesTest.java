package com.morak.back.core.domain.times;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalTimesTest {

    private final Times times = new LocalTimes();

    @BeforeEach
    void setUp() {
        times.reset();
    }

    @Test
    void 현재_시각을_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        times.changeTime(now);

        // when
        LocalDateTime dateTimeOfNow = times.dateTimeOfNow();

        // then
        assertThat(dateTimeOfNow).isEqualTo(now);
    }

    @Test
    void 현재_날짜를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        times.changeTime(now);

        // when
        LocalDate dateOfNow = times.dateOfNow();

        // then
        assertThat(dateOfNow).isEqualTo(now.toLocalDate());
    }
}
