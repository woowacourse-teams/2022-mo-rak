package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AvailableTimesTest {

    @Test
    void 약속을_선택할_수_있다() {
        // given
        long memberId = 1L;
        AvailableTimes availableTimes = new AvailableTimes();
        LocalDateTime localDateTime = LocalDateTime.now();

        // when
        availableTimes.select(Set.of(localDateTime), memberId);

        // then
        assertThat(availableTimes.getAvailableTimes())
                .containsExactly(AvailableTime.builder()
                        .memberId(memberId)
                        .startDateTime(localDateTime)
                        .build()
                );
    }

    @Test
    void 약속을_재선택할_수_있다() {
        // given
        long memberId = 1L;
        AvailableTimes availableTimes = new AvailableTimes();
        LocalDateTime localDateTime = LocalDateTime.now();

        // when
        availableTimes.select(Set.of(localDateTime), memberId);
        availableTimes.select(Set.of(localDateTime.plusDays(1)), memberId);

        // then
        assertThat(availableTimes.getAvailableTimes())
                .containsExactly(AvailableTime.builder()
                        .memberId(memberId)
                        .startDateTime(localDateTime.plusDays(1))
                        .build()
                );
    }

    @Test
    void 멤버가_약속선택을_진행했는지_확인한다() {
        // given
        long memberId = 1L;
        AvailableTimes availableTimes = new AvailableTimes();
        availableTimes.select(Set.of(LocalDateTime.now()), memberId);

        // when
        boolean actual = availableTimes.hasMember(memberId);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 멤버가_약속선택을_진행_안했는지_확인한다() {
        // given
        long memberId = 1L;
        AvailableTimes availableTimes = new AvailableTimes();
        availableTimes.select(Set.of(LocalDateTime.now()), memberId);

        // when
        boolean actual = availableTimes.hasMember(memberId + 1L);

        // then
        assertThat(actual).isFalse();
    }
}