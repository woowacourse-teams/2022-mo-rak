package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.appointment.domain.recommend.AppointmentTime;
import com.morak.back.appointment.domain.recommend.RecommendationCell;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecommendationCellTest {

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @Test
    void RecommendationCell을_생성한다() {
        // given
        AppointmentTime appointmentTime = new AppointmentTime(LocalDateTime.of(2022, 10, 18, 10, 0), 60);
        List<Long> memberIds = List.of(1L, 2L, 3L);

        // when & then
        assertThatNoException().isThrownBy(() -> RecommendationCell.of(appointmentTime, memberIds));
    }

    @Test
    void 약속잡기_가능_시간이_들어오면_계산한다() {
        // given
        RecommendationCell cell = RecommendationCell.of(
                new AppointmentTime(LocalDateTime.of(2022, 10, 18, 10, 0), 60),
                List.of(1L, 2L, 3L)
        );
        Set<AvailableTime> availableTimes = Set.of(
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build(),
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 30)).build(),
                AvailableTime.builder().memberId(2L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build()
        );

        // when
        cell.calculate(availableTimes);

        // then
        Map<Long, Integer> memberIdScores = cell.getMemberIdScores();
        Assertions.assertAll(
                () -> assertThat(memberIdScores.get(1L)).isEqualTo(2),
                () -> assertThat(memberIdScores.get(2L)).isEqualTo(1)
        );
    }

    @Test
    void 계산된_점수를_합산한다() {
        // given
        RecommendationCell cell = RecommendationCell.of(
                new AppointmentTime(LocalDateTime.of(2022, 10, 18, 10, 0), 60),
                List.of(1L, 2L, 3L)
        );
        Set<AvailableTime> availableTimes = Set.of(
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build(),
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 30)).build(),
                AvailableTime.builder().memberId(2L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build()
        );
        cell.calculate(availableTimes);

        // when
        int score = cell.sumScore();

        // then
        assertThat(score).isEqualTo(3);
    }

    @Test
    void 추천셀에_포함된_멤버가_있는지_확인한다() {
        // given
        RecommendationCell cell = RecommendationCell.of(
                new AppointmentTime(LocalDateTime.of(2022, 10, 18, 10, 0), 60),
                List.of(1L, 2L, 3L)
        );
        Set<AvailableTime> availableTimes = Set.of(
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build(),
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 30)).build(),
                AvailableTime.builder().memberId(2L).startDateTime(LocalDateTime.of(2022, 10, 18, 10, 0)).build()
        );
        cell.calculate(availableTimes);

        // when
        boolean hasAnyMembers = cell.hasAnyMembers();

        // then
        assertThat(hasAnyMembers).isTrue();
    }

    @Test
    void 마지막_시간에_자정이_포함된_recommendationCell을_계산한다() {
        // given
        RecommendationCell cell = RecommendationCell.of(
                new AppointmentTime(LocalDateTime.of(2022, 10, 18, 23, 0), 60),
                List.of(1L, 2L, 3L)
        );
        Set<AvailableTime> availableTimes = Set.of(
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 23, 0)).build(),
                AvailableTime.builder().memberId(1L).startDateTime(LocalDateTime.of(2022, 10, 18, 23, 30)).build(),
                AvailableTime.builder().memberId(2L).startDateTime(LocalDateTime.of(2022, 10, 18, 23, 0)).build()
        );
        cell.calculate(availableTimes);

        // when
        int score = cell.sumScore();

        // then
        assertThat(score).isEqualTo(3);
    }
}
