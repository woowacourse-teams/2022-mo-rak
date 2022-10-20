package com.morak.back.appointment.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static com.morak.back.SimpleMockMvc.delete;
import static com.morak.back.SimpleMockMvc.get;
import static com.morak.back.SimpleMockMvc.patch;
import static com.morak.back.SimpleMockMvc.post;
import static com.morak.back.SimpleMockMvc.put;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.ui.dto.AppointmentAllResponse;
import com.morak.back.appointment.ui.dto.AppointmentCreateRequest;
import com.morak.back.appointment.ui.dto.AppointmentResponse;
import com.morak.back.appointment.ui.dto.AvailableTimeRequest;
import com.morak.back.poll.ui.ControllerTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest extends ControllerTest {

    private static final String GROUP_CODE = "MoraK123";
    private static final String APPOINTMENT_CODE = "FJn3ND26";

    @MockBean
    private AppointmentService appointmentService;

    @Test
    void 약속잡기를_생성한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments";
        AppointmentCreateRequest request = new AppointmentCreateRequest(
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                LocalDate.of(2022, 8, 5),
                LocalDate.of(2022, 8, 20),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                2,
                30,
                LocalDateTime.now().plusDays(1));

        given(appointmentService.createAppointment(anyString(), anyLong(), any())).willReturn("KDIs23K3");

        // when
        ResultActions response = post(mockMvc, path, GROUP_CODE, request);

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/groups/MoraK123/appointments/KDIs23K3"))
                .andDo(document("appointment/create-appointment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupCode").description("그룹_코드"))
                ));
    }

    @Test
    void 약속잡기_목록을_조회한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments";

        AppointmentAllResponse response1 = new AppointmentAllResponse(
                1L,
                "FJn3ND26",
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                2,
                30,
                LocalDateTime.now().plusMinutes(30),
                false,
                2
        );

        AppointmentAllResponse response2 = new AppointmentAllResponse(
                2L,
                "j3KDcd2h",
                "스터디 회의 시간",
                "스터디 진행과 관련된 회의입니다.",
                2,
                0,
                LocalDateTime.now().plusMinutes(30),
                true,
                5
        );

        given(appointmentService.findAppointments(anyString(), anyLong())).willReturn(List.of(response1, response2));

        // when
        ResultActions response = get(mockMvc, path, GROUP_CODE);

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("appointment/get-appointments",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드")
                        )
                ));
    }

    @Test
    void 약속잡기_단건을_조회한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}";

        AppointmentResponse findResponse = new AppointmentResponse(
                1L,
                "FJn3ND26",
                "모락 회식 날짜 및 시간",
                "필참입니다.",
                2,
                30,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalTime.of(16, 0),
                LocalTime.of(20, 0),
                LocalDateTime.now().plusMinutes(30),
                false,
                true
        );

        given(appointmentService.findAppointment(anyString(), anyLong(), anyString())).willReturn(findResponse);

        // when
        ResultActions response = get(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE);

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("appointment/get-appointment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )));
    }

    @Test
    void 약속잡기_선택을_진행한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}";
        List<AvailableTimeRequest> requests = List.of(
                new AvailableTimeRequest(
                        LocalDateTime.of(2022, 8, 6, 16, 0)
                ),
                new AvailableTimeRequest(
                        LocalDateTime.of(2022, 8, 6, 16, 30)
                )
        );

        // when
        ResultActions response = put(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE, requests);

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("appointment/select-available-time",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )
                ));
    }

    @Test
    void 약속잡기_추천_결과를_조회한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}/recommendation";

        // when
        ResultActions response = get(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE);

        response
                .andExpect(status().isOk())
                .andDo(document("appointment/recommend-appointments",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )
                ));
    }

    @Test
    void 약속잡기가_마감되었는지_확인한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}/close";

        // when
        ResultActions response = patch(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE);

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("appointment/close-appointment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )
                ));
    }

    @Test
    void 약속잡기를_삭제한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}";

        // when
        ResultActions response = delete(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE);

        // then
        response
                .andExpect(status().isNoContent())
                .andDo(document("appointment/delete-appointment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )
                ));
    }

    @Test
    void 약속잡기가_진행중인지_확인한다() throws Exception {
        // given
        String path = "/api/groups/{groupCode}/appointments/{appointmentCode}/status";

        // when
        ResultActions response = get(mockMvc, path, GROUP_CODE, APPOINTMENT_CODE);

        // then
        response.andExpect(status().isOk())
                .andDo(document("appointment/get-appointment-status",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("appointmentCode").description("약속잡기_코드")
                        )
                ));
    }
}
