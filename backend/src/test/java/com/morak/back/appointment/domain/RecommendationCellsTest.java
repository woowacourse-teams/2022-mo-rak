//package com.morak.back.appointment.domain;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatNoException;
//
//import com.morak.back.appointment.domain.Appointment.AppointmentBuilder;
//import com.morak.back.auth.domain.Member;
//import com.morak.back.core.domain.Code;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class RecommendationCellsTest {
//
//    private static AppointmentBuilder DEFAULT_BUILDER;
//
//    private Member memberA;
//    private Member memberB;
//
//    @BeforeEach
//    void setUp() {
//        DEFAULT_BUILDER = Appointment.builder()
//                .title("회식 날짜")
//                .description("필참입니다.")
//                .code(Code.generate(length -> "FJn3ND26"))
//                .closedAt(LocalDateTime.now().plusDays(1))
//                .startDate(LocalDate.now().plusDays(1))
//                .endDate(LocalDate.now().plusDays(5))
//                .startTime(LocalTime.of(14, 0))
//                .endTime(LocalTime.of(20, 0))
//                .durationHours(2)
//                .durationMinutes(0)
//                .closedAt(LocalDateTime.now().plusDays(1));
//        memberA = Member.builder()
//                .id(1L)
//                .name("멤버A")
//                .build();
//        memberB = Member.builder()
//                .id(2L)
//                .name("멤버B")
//                .build();
//    }
//
//    @Test
//    void RecommendationCells를_약속잡기로_생성한다() {
//        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//
//        // when & then
//        assertThatNoException().isThrownBy(
//                () -> RecommendationCells.of(appointment, List.of(memberA, memberB))
//        );
//    }
//
//    @Test
//    void 약속잡기_시간범위가_2시간30분이고_진행시간이_2시간이면_두개의_셀이_생성된다() {
//        // given
//        Appointment appointment = DEFAULT_BUILDER.startDate(LocalDate.now().plusDays(1))
//                .endDate(LocalDate.now().plusDays(1))
//                .startTime(LocalTime.of(14, 0))
//                .endTime(LocalTime.of(16, 30))
//                .durationHours(2)
//                .durationMinutes(0)
//                .closedAt(LocalDateTime.now().plusMinutes(30))
//                .build();
//
//        // when
//        RecommendationCells cells = RecommendationCells.of(appointment, List.of(memberA, memberB));
//
//        // then
//        assertThat(cells.getCells()).hasSize(2);
//    }
//
//    @Test
//    void 약속잡기_추천_목록을_생성한다() {
//        // given
//        Appointment appointment = DEFAULT_BUILDER.build();
//        RecommendationCells cells = RecommendationCells.of(appointment, List.of(memberA, memberB));
//
//        // when
//        AvailableTime availableTime = AvailableTime.builder()
//                .member(memberA)
//                .appointment(appointment)
//                .startDateTime(appointment.getStartDateTime().plusHours(3))
//                .endDateTime(appointment.getStartDateTime().plusHours(3).plusMinutes(30))
//                .build();
//
//        List<RankRecommendation> recommend = cells.recommend(List.of(availableTime));
//
//        // then
//        assertThat(recommend).hasSize(4);
//    }
//}
