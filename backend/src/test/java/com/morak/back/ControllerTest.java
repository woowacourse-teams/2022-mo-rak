package com.morak.back;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morak.back.appointment.application.AppointmentService;
import com.morak.back.appointment.ui.AppointmentController;
import com.morak.back.auth.application.OAuthService;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.OAuthController;
import com.morak.back.core.ui.GlobalController;
import com.morak.back.notification.application.WebhookService;
import com.morak.back.notification.ui.NotificationController;
import com.morak.back.performance.PerformanceMonitor;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.ui.PollController;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.ui.RoleController;
import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.TeamController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@AutoConfigureDataJpa
@WebMvcTest({
        GlobalController.class,
        OAuthController.class,
        TeamController.class,
        PollController.class,
        AppointmentController.class,
        RoleController.class,
        NotificationController.class,
})
@MockBean({
        OAuthService.class,
        TeamService.class,
        AppointmentService.class,
        PollService.class,
        RoleService.class,
        WebhookService.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected PerformanceMonitor performanceMonitor;

    @BeforeEach
    void setUp() {
        given(tokenProvider.parsePayload(anyString())).willReturn(String.valueOf(1L));
    }
}
