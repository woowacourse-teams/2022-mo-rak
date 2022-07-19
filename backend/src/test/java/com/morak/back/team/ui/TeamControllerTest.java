package com.morak.back.team.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamController.class)
@AutoConfigureRestDocs
@AutoConfigureDataJpa
class TeamControllerTest {

    @MockBean
    private TeamService teamService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private TeamController teamController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        doNothing().when(tokenProvider).validateToken(anyString());
        given(tokenProvider.parsePayload(anyString())).willReturn("1");
    }

    @Test
    void 그룹을_생성한다() throws Exception {
        // given
        TeamCreateRequest request = new TeamCreateRequest("team-name");
        given(teamService.createTeam(anyLong(), any())).willReturn("ABCD1234");
        // when

        mockMvc.perform(
                post("/api/groups")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/groups/ABCD1234")
        ).andDo(
                document(
                        "group-create",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
        // then
    }
}