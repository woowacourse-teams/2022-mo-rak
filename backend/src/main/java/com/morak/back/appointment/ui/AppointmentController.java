package com.morak.back.appointment.ui;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.support.Auth;
import com.morak.back.auth.ui.dto.MemberResponse;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupCode}/appointments")
public class AppointmentController {

    @PostMapping
    public ResponseEntity<Void> createAppointment(@PathVariable String groupCode,
                                                  @Auth Long memberId,
                                                  @RequestBody AppointmentCreateRequest request) {
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/appointments/" + 1)).build();
    }

    @GetMapping
    public ResponseEntity<List<AppointmentAllResponse>> findAppointments(@PathVariable String groupCode,
                                                                         @Auth Long memberId) {
        Appointment appointment1 = Appointment.builder()
                .code("FJn3ND26")
                .title("모락 회식 날짜 및 시간")
                .description("필참입니다.")
                .startDate(LocalDate.of(2022, 8, 5))
                .endDate(LocalDate.of(2022, 8, 20))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(30)
                .build();

        Appointment appointment2 = Appointment.builder()
                .code("j3KDcd2h")
                .title("스터디 회의 시간")
                .description("스터디 진행과 관련된 회의입니다.")
                .startDate(LocalDate.of(2022, 8, 5))
                .endDate(LocalDate.of(2022, 8, 20))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(0)
                .build();
        return ResponseEntity.ok(
                List.of(AppointmentAllResponse.from(appointment1), AppointmentAllResponse.from(appointment2)));
    }

    @GetMapping("/{appointmentCode}")
    public ResponseEntity<AppointmentResponse> findAppointment(@PathVariable String groupCode,
                                                               @Auth Long memberId,
                                                               @PathVariable String appointmentCode) {
        Appointment appointment = Appointment.builder()
                .code("FJn3ND26")
                .title("모락 회식 날짜 및 시간")
                .description("필참입니다.")
                .startDate(LocalDate.of(2022, 8, 5))
                .endDate(LocalDate.of(2022, 8, 20))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(20, 0))
                .durationHours(2)
                .durationMinutes(30)
                .build();
        return ResponseEntity.ok(AppointmentResponse.from(appointment));
    }

    @PutMapping("/{appointmentCode}")
    public ResponseEntity<Void> selectAvailableTimes(@PathVariable String groupCode,
                                                     @Auth Long memberId,
                                                     @PathVariable String appointmentCode,
                                                     @RequestBody List<AvailableTimeRequest> requests) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{appointmentCode}/recommendation")
    public ResponseEntity<List<RecommendationResponse>> recommendAppointments(@PathVariable String groupCode,
                                                                              @Auth Long memberId,
                                                                              @PathVariable String appointmentCode) {
        RecommendationResponse recommendationResponse1 = new RecommendationResponse(
                1,
                LocalDateTime.of(2022, 8, 6, 16, 0),
                LocalDateTime.of(2022, 8, 6, 18, 30),
                List.of(
                        new MemberResponse(1L, "eden", "eden-profile.com"),
                        new MemberResponse(2L, "ellie", "ellie-profile.com")
                ),
                List.of(new MemberResponse(5L, "albur", "albur-profile.com"))
        );
        RecommendationResponse recommendationResponse2 = new RecommendationResponse(
                1,
                LocalDateTime.of(2022, 8, 6, 17, 0),
                LocalDateTime.of(2022, 8, 6, 19, 30),
                List.of(new MemberResponse(1L, "eden", "eden-profile.com")),
                List.of(
                        new MemberResponse(5L, "albur", "albur-profile.com"),
                        new MemberResponse(2L, "ellie", "ellie-profile.com")
                )
        );
        return ResponseEntity.ok(List.of(recommendationResponse1, recommendationResponse2));
    }

    @PatchMapping("/{appointmentCode}/close")
    public ResponseEntity<Void> closeAppointment(@PathVariable String groupCode,
                                                 @Auth Long memberId,
                                                 @PathVariable String appointmentCode) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{appointmentCode}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String groupCode,
                                                  @Auth Long memberId,
                                                  @PathVariable String appointmentCode) {
        return ResponseEntity.noContent().build();
    }
}
