package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import java.time.LocalDateTime;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AvailableTimeRepositoryTest {

    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Member member;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L).orElseThrow();
        appointment = appointmentRepository.findByCode("FEsd23C1").orElseThrow();
    }

    @Test
    void 약속잡기_가능_시간을_저장한다() {
        // given
        AvailableTime availableTime = AvailableTime.builder()
                .member(member)
                .appointment(appointment)
                .startDateTime(LocalDateTime.now().plusDays(1))
                .endDateTime(LocalDateTime.now().plusDays(1).plusMinutes(30))
                .build();

        // when
        AvailableTime savedAvailableTime = availableTimeRepository.save(availableTime);

        // then
        assertThat(savedAvailableTime.getId()).isNotNull();
    }

    @Test
    void 약속잡기_가능_시작_시점이_현재보다_과거일_경우_예외를_던진다() {
        // given
        AvailableTime availableTime = AvailableTime.builder()
                .member(member)
                .appointment(appointment)
                .startDateTime(LocalDateTime.now().minusDays(1))
                .endDateTime(LocalDateTime.now().minusDays(1).plusMinutes(30))
                .build();

        // when & then
        assertThatThrownBy(() -> availableTimeRepository.save(availableTime))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
