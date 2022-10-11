//package com.morak.back.appointment.domain;
//
//import static com.morak.back.appointment.domain.DomainFixture.까라;
//import static com.morak.back.appointment.domain.DomainFixture.리엘;
//import static com.morak.back.appointment.domain.DomainFixture.시작_시간;
//import static com.morak.back.appointment.domain.DomainFixture.약속잡기_회식_날짜;
//import static com.morak.back.appointment.domain.DomainFixture.에덴;
//import static com.morak.back.appointment.domain.DomainFixture.한시간_일정;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//class RankRecommendationTest {
//
//    @Test
//    void 가능한_멤버와_설득할_멤버를_찾는다() {
//        // given
//        RecommendationCell recommendationCell = RecommendationCell.of(시작_시간, 한시간_일정, List.of(에덴, 까라, 리엘));
//
//        // when
//        AvailableTime edenAvailableTime1 = AvailableTime.builder()
//                .member(에덴)
//                .appointment(약속잡기_회식_날짜)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime edenAvailableTime2 = AvailableTime.builder()
//                .member(에덴)
//                .appointment(약속잡기_회식_날짜)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
//                .build();
//
//        AvailableTime karaAvailableTime = AvailableTime.builder()
//                .member(까라)
//                .appointment(약속잡기_회식_날짜)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime ellieAvailableTime = AvailableTime.builder()
//                .member(리엘)
//                .appointment(약속잡기_회식_날짜)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(19, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(19, 30)))
//                .build();
//
//        List<AvailableTime> availableTimes = List.of(edenAvailableTime1, edenAvailableTime2, karaAvailableTime,
//                ellieAvailableTime);
//
//        recommendationCell.calculate(availableTimes);
//
//        // when
//        RankRecommendation rankRecommendation = RankRecommendation.from(1, recommendationCell);
//
//        // then
//        Assertions.assertAll(
//                () -> assertThat(rankRecommendation.getAvailableMembers()).hasSize(1),
//                () -> assertThat(rankRecommendation.getUnavailableMembers()).hasSize(2)
//        );
//    }
//}
