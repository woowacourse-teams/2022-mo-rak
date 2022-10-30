package com.morak.back.appointment.ui;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AppointmentStatusResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.appointment.ui.dto.RecommendationResponse;
import com.morak.back.auth.support.Auth;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
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

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@PathVariable String groupCode,
                                                                 @Auth Long memberId,
                                                                 @Valid @RequestBody AppointmentCreateRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(groupCode, memberId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/appointments/" + response.getCode()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentAllResponse>> findAppointments(@PathVariable String groupCode,
                                                                         @Auth Long memberId) {
        return ResponseEntity.ok(appointmentService.findAppointments(groupCode, memberId));
    }

    @GetMapping("/{appointmentCode}")
    public ResponseEntity<AppointmentResponse> findAppointment(@PathVariable String groupCode,
                                                               @Auth Long memberId,
                                                               @PathVariable String appointmentCode) {
        return ResponseEntity.ok(appointmentService.findAppointment(groupCode, memberId, appointmentCode));
    }

    @PutMapping("/{appointmentCode}")
    public ResponseEntity<Void> selectAvailableTimes(@PathVariable String groupCode,
                                                     @Auth Long memberId,
                                                     @PathVariable String appointmentCode,
                                                     @Valid @RequestBody List<AvailableTimeRequest> requests) {
        appointmentService.selectAvailableTimes(groupCode, memberId, appointmentCode, requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{appointmentCode}/recommendation")
    public ResponseEntity<List<RecommendationResponse>> recommendAppointmentTimes(@PathVariable String groupCode,
                                                                                  @Auth Long memberId,
                                                                                  @PathVariable String appointmentCode) {
        return ResponseEntity.ok(appointmentService.recommendAppointmentTimes(groupCode, memberId, appointmentCode));
    }

    @PatchMapping("/{appointmentCode}/close")
    public ResponseEntity<Void> closeAppointment(@PathVariable String groupCode,
                                                 @Auth Long memberId,
                                                 @PathVariable String appointmentCode) {
        appointmentService.closeAppointment(groupCode, memberId, appointmentCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{appointmentCode}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String groupCode,
                                                  @Auth Long memberId,
                                                  @PathVariable String appointmentCode) {
        appointmentService.deleteAppointment(groupCode, memberId, appointmentCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{appointmentCode}/status")
    public ResponseEntity<AppointmentStatusResponse> findAppointmentStatus(@PathVariable String groupCode,
                                                                           @Auth Long memberId,
                                                                           @PathVariable String appointmentCode) {
        AppointmentStatusResponse response = appointmentService.findAppointmentStatus(groupCode, memberId,
                appointmentCode);
        return ResponseEntity.ok(response);
    }
}
