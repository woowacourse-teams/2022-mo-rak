package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
import com.morak.back.appointment.domain.recommend.RankRecommendation;
import com.morak.back.appointment.domain.recommend.RecommendationCells;
import com.morak.back.core.domain.Code;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecommendationCellsTest {

    private static AppointmentBuilder DEFAULT_BUILDER;

    private LocalDate today;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        now = LocalDateTime.now();

        DEFAULT_BUILDER = Appointment.builder()
                .title("회식 날짜")
                .subTitle("필참입니다.")
                .code(Code.generate(length -> "FJn3ND26"))
                .teamCode(Code.generate(length -> "teAmCoDe"))
                .closedAt(LocalDateTime.now().plusDays(1))
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusDays(1))
                .now(now);
    }

    @Test
    void RecommendationCells를_약속잡기로_생성한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();

        // when & then
        assertThatNoException().isThrownBy(
                () -> RecommendationCells.of(appointment, List.of(1L, 2L))
        );
    }

    @Test
    void 약속잡기_시간범위가_2시간30분이고_진행시간이_2시간이면_두개의_셀이_생성된다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.startDate(today.plusDays(1))
                .endDate(today.plusDays(1))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 30))
                .durationHours(2)
                .durationMinutes(0)
                .closedAt(LocalDateTime.now().plusMinutes(30))
                .build();

        // when
        RecommendationCells cells = RecommendationCells.of(appointment, List.of(1L, 2L));

        // then
        assertThat(cells.getCells()).hasSize(2);
    }

    @Test
    void 약속잡기_추천_목록을_생성한다() {
        // given
        Appointment appointment = DEFAULT_BUILDER.build();
        RecommendationCells cells = RecommendationCells.of(appointment, List.of(1L, 2L));
        AvailableTime availableTime = AvailableTime.builder()
                .memberId(1L)
                .startDateTime(LocalDateTime.of(today.plusDays(2), LocalTime.of(16, 0)))
                .build();

        // when
        List<RankRecommendation> recommend = cells.recommend(Set.of(availableTime));

        // then
        assertThat(recommend).hasSize(4);
    }
}
