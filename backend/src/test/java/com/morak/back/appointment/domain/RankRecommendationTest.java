package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.recommend.AppointmentTime;
import com.morak.back.appointment.domain.recommend.RankRecommendation;
import com.morak.back.appointment.domain.recommend.RecommendationCell;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RankRecommendationTest {

    @Test
    void 가능한_멤버와_설득할_멤버를_찾는다() {
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
        RankRecommendation rankRecommendation = RankRecommendation.from(1, cell);

        // then
        Assertions.assertAll(
                () -> assertThat(rankRecommendation.getAvailableMembers()).hasSize(1),
                () -> assertThat(rankRecommendation.getUnavailableMembers()).hasSize(2)
        );
    }
}
