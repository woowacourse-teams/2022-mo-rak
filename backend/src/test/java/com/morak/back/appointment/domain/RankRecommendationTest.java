package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.DomainFixture.까라;
import static com.morak.back.appointment.domain.DomainFixture.리엘;
import static com.morak.back.appointment.domain.DomainFixture.시작_시간;
import static com.morak.back.appointment.domain.DomainFixture.약속잡기_회식_날짜;
import static com.morak.back.appointment.domain.DomainFixture.에덴;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.availabletime.AvailableTime;
import com.morak.back.appointment.domain.recommand.RankRecommendation;
import com.morak.back.appointment.domain.recommand.RecommendationCell;
import com.morak.back.appointment.domain.recommand.RecommendationDateTimePeriod;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.core.domain.times.Times;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RankRecommendationTest {

    private final Times times = new LocalTimes();

    @Test
    void 가능한_멤버와_설득할_멤버를_찾는다() {
        // given
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 시작_시간.plusHours(1)), List.of(에덴, 까라, 리엘));

        // when
        AvailableTime edenAvailableTime1 = AvailableTime.builder()
                .member(에덴)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간)
                .endDateTime(시작_시간.plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime edenAvailableTime2 = AvailableTime.builder()
                .member(에덴)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간.plusHours(1))
                .endDateTime(시작_시간.plusHours(1).plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간)
                .endDateTime(시작_시간.plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime2 = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간.plusMinutes(30))
                .endDateTime(시작_시간.plusHours(1))
                .now(LocalDateTime.now())
                .build();

        AvailableTime ellieAvailableTime = AvailableTime.builder()
                .member(리엘)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간.plusHours(3))
                .endDateTime(시작_시간.plusHours(3).plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        List<AvailableTime> availableTimes = List.of(edenAvailableTime1, edenAvailableTime2,
                karaAvailableTime, karaAvailableTime2, ellieAvailableTime);

        recommendationCell.calculate(availableTimes);

        // when
        RankRecommendation rankRecommendation = RankRecommendation.from(1, recommendationCell, 30);

        // then
        Assertions.assertAll(
                () -> assertThat(rankRecommendation.getAvailableMembers()).hasSize(1),
                () -> assertThat(rankRecommendation.getUnavailableMembers()).hasSize(2)
        );
    }
}
