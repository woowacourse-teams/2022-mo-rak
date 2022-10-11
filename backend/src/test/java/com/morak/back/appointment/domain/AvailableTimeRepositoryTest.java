//package com.morak.back.appointment.domain;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.morak.back.auth.domain.Member;
//import com.morak.back.auth.domain.MemberRepository;
//import com.morak.back.core.domain.Code;
//import com.morak.back.support.RepositoryTest;
//import com.morak.back.team.domain.Team;
//import com.morak.back.team.domain.TeamRepository;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//
//@RepositoryTest
//class AvailableTimeRepositoryTest {
//
//    @Autowired
//    private AvailableTimeRepository availableTimeRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    private Member member;
//    private Appointment appointment;
//
//    @BeforeEach
//    void setUp(@Autowired MemberRepository memberRepository, @Autowired TeamRepository teamRepository) {
//        member = memberRepository.findById(1L).orElseThrow();
//        Team team = teamRepository.findByCode("MoraK123").orElseThrow();
//
//        this.appointment = appointmentRepository.save(
//                Appointment.builder()
//                        .host(member)
//                        .team(team)
//                        .title("회식 날짜")
//                        .description("필참!!")
//                        .startDate(LocalDate.now().plusDays(1))
//                        .endDate(LocalDate.now().plusDays(5))
//                        .startTime(LocalTime.of(14, 0))
//                        .endTime(LocalTime.of(18, 30))
//                        .durationHours(1)
//                        .durationMinutes(0)
//                        .closedAt(LocalDateTime.now().plusDays(1))
//                        .code(Code.generate(length -> "FJn3ND26"))
//                        .closedAt(LocalDateTime.now().plusDays(1))
//                        .build()
//        );
//    }
//
//    // todo : fix this
//    @Test
//    @Disabled
//    void 약속잡기_가능_시간을_저장한다() {
//        // given
//        AvailableTime availableTime = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        // when
////        AvailableTime savedAvailableTime = availableTimeRepository.save(availableTime);
//
//        // then
////        assertThat(savedAvailableTime.getId()).isNotNull();
//    }
//
//    @Test
//    @Disabled // todo : fix this
//    void 약속잡기_가능_시간을_모두_저장한다() {
//        // given
//        AvailableTime availableTime1 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime availableTime2 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
//                .build();
//
//        // when
////        List<AvailableTime> availableTimes = availableTimeRepository.saveAll(List.of(availableTime1, availableTime2));
//
//        // then
////        Assertions.assertAll(
////                () -> assertThat(availableTimes).hasSize(2)
////                () -> assertThat(availableTimes).allMatch(availableTime -> availableTime.getId() != null)
////        );
//    }
//
//    @Test
//    @Disabled // todo : fix this
//    void 같은_약속잡기_가능_시간을_저장하는_경우_예외를_던진다() {
//        // given
//        AvailableTime availableTime1 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime availableTime2 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        // when & then
////        assertThatThrownBy(() -> availableTimeRepository.saveAll(List.of(availableTime1, availableTime2)))
////                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @Disabled // todo : fix this
//    void 멤버_id와_약속잡기_id로_약속잡기_가능_시간을_모두_삭제한다() {
//        // given
//        AvailableTime availableTime1 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime availableTime2 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
//                .build();
//
////        availableTimeRepository.saveAll(List.of(availableTime1, availableTime2));
//
//        // when
////        availableTimeRepository.deleteAllByMemberAndAppointment(member, appointment);
//
//        // then
////        List<AvailableTime> availableTimes = availableTimeRepository.findAllByMemberAndAppointment(member, appointment);
////        assertThat(availableTimes).isEmpty();
//    }
//
//    @Test
//    @Disabled // todo : fix this
//    void 약속잡기_id로_약속잡기_가능_시간을_모두_삭제한다() {
//        // given
//        AvailableTime availableTime1 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .build();
//
//        AvailableTime availableTime2 = AvailableTime.builder()
//                .member(member)
//                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)))
//                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
//                .build();
//
////        availableTimeRepository.saveAll(List.of(availableTime1, availableTime2));
//
//        // when
////        availableTimeRepository.deleteAllByAppointment(appointment);
//
//        // then
////        List<AvailableTime> availableTimes = availableTimeRepository.findAllByAppointment(appointment);
////        assertThat(availableTimes).isEmpty();
//    }
//}
