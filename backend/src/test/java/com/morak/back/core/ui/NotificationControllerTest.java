package com.morak.back.core.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.ControllerTest;
import com.morak.back.notification.application.WebhookService;
import com.morak.back.notification.application.dto.SlackWebhookCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class NotificationControllerTest extends ControllerTest {

    @MockBean
    private WebhookService webhookService;

    @Test
    void 슬랙_웹훅을_등록한다() throws Exception {
        // given
        SlackWebhookCreateRequest request = new SlackWebhookCreateRequest("https://hooks.slack.com/services/");

        given(webhookService.saveSlackWebhook(anyString(), anyLong(), any(SlackWebhookCreateRequest.class)))
                .willReturn(1L);

        // when
        ResultActions response = mockMvc.perform(post("/api/groups/{groupCode}/slack", "MoraK123")
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        response.andExpect(status().isCreated())
                .andDo(document(
                        "slack",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}
