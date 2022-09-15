package com.morak.back.appointment.domain;

import static com.morak.back.appointment.domain.DomainFixture.까라;
import static com.morak.back.appointment.domain.DomainFixture.끝_시간;
import static com.morak.back.appointment.domain.DomainFixture.시작_시간;
import static com.morak.back.appointment.domain.DomainFixture.약속잡기_회식_날짜;
import static com.morak.back.appointment.domain.DomainFixture.에덴;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.appointment.domain.availabletime.AvailableTime;
import com.morak.back.appointment.domain.recommand.RecommendationCell;
import com.morak.back.appointment.domain.recommand.RecommendationDateTimePeriod;
import com.morak.back.auth.domain.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RecommendationCellTest {

    @Test
    void RecommendationCell을_생성한다() {
        // given

        // when
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 끝_시간), List.of(에덴, 까라));

        // then
        Assertions.assertAll(
                () -> assertThat(recommendationCell.getRecommendationDateTimePeriod().getStartDateTime()).isEqualTo(
                        시작_시간),
                () -> assertThat(recommendationCell.getRecommendationDateTimePeriod().getEndDateTime()).isEqualTo(끝_시간),
                () -> assertThat(recommendationCell.getMemberScores()).hasSize(2),
                () -> assertThat(recommendationCell.getMemberScores().get(에덴)).isEqualTo(0)
        );
    }

    @Test
    void 약속잡기_가능_시간이_들어오면_계산한다() {
        // given
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 끝_시간), List.of(에덴, 까라));

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
                .startDateTime(끝_시간.plusDays(3))
                .endDateTime(끝_시간.plusDays(3).plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime1 = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간)
                .endDateTime(시작_시간.plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime2 = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간.plusHours(1))
                .endDateTime(시작_시간.plusHours(1).plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        List<AvailableTime> availableTimes = List.of(edenAvailableTime1, edenAvailableTime2,
                karaAvailableTime1, karaAvailableTime2);

        // when
        recommendationCell.calculate(availableTimes);

        // then
        Map<Member, Integer> memberScores = recommendationCell.getMemberScores();
        Assertions.assertAll(
                () -> assertThat(memberScores.get(에덴)).isEqualTo(1),
                () -> assertThat(memberScores.get(까라)).isEqualTo(2)
        );
    }

    @Test
    void 계산된_점수를_합산한다() {
        // given
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 끝_시간), List.of(에덴, 까라));

        AvailableTime edenAvailableTime = AvailableTime.builder()
                .member(에덴)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간)
                .endDateTime(시작_시간.plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(끝_시간.minusMinutes(30))
                .endDateTime(끝_시간)
                .now(LocalDateTime.now())
                .build();

        List<AvailableTime> availableTimes = List.of(edenAvailableTime, karaAvailableTime);

        // when
        recommendationCell.calculate(availableTimes);

        // then
        assertThat(recommendationCell.sumScore()).isEqualTo(2);
    }

    @Test
    void 추천셀에_포함된_멤버가_있는지_확인한다() {
        // given
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 끝_시간), List.of(에덴, 까라));

        AvailableTime notOverlappedAvailableTime = AvailableTime.builder()
                .member(에덴)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(시작_시간)
                .endDateTime(시작_시간.plusMinutes(30))
                .now(LocalDateTime.now())
                .build();

        List<AvailableTime> availableTimes = List.of(notOverlappedAvailableTime);

        // when
        recommendationCell.calculate(availableTimes);

        // then
        assertThat(recommendationCell.hasAnyMembers()).isTrue();
    }

    @Test
    void 마지막_시간에_자정이_포함된_recommendationCell을_계산한다() {
        // given
        RecommendationCell recommendationCell = RecommendationCell.of(
                new RecommendationDateTimePeriod(시작_시간, 끝_시간), List.of(에덴, 까라));

        // when
        AvailableTime edenAvailableTime = AvailableTime.builder()
                .member(에덴)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
                .now(LocalDateTime.now())
                .build();

        AvailableTime karaAvailableTime = AvailableTime.builder()
                .member(까라)
                .appointment(약속잡기_회식_날짜)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
                .now(LocalDateTime.now())
                .build();
        List<AvailableTime> availableTimes = List.of(edenAvailableTime, karaAvailableTime);
        recommendationCell.calculate(availableTimes);

        // then
        assertThat(recommendationCell.sumScore()).isEqualTo(2);
    }
}
