package com.morak.back.team.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morak.back.auth.application.TokenProvider;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        // when & then
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
    }

    @Test
    void 그룹_초대코드를_생성한다() throws Exception {
        // given
        given(teamService.createInvitationCode(anyLong(), anyString())).willReturn("ABCDE12345");

        // when & then
        mockMvc.perform(
                post("/api/groups/{group-code}/invitation", "ABCD1234")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/groups/in/ABCDE12345")
        ).andDo(
                document(
                        "group-invitation-code-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("group-code").description("그룹 코드")
                        )
                )
        );
    }

    @Test
    void 그룹_가입여부를_확인한다() throws Exception {
        // given
        given(teamService.isJoined(anyLong(), anyString())).willReturn(
                new InvitationJoinedResponse("ABCD1234", "모락", true));

        // when & then
        mockMvc.perform(
                get("/api/groups/in/{invitation-code}", "ABCDE12345")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                document(
                        "group-isJoined",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("invitation-code").description("그룹 초대 코드")
                        )
                )
        );
    }

    @Test
    void 그룹_목록을_조회한다() throws Exception {
        // given
        given(teamService.findTeams(anyLong())).willReturn(
                List.of(new TeamResponse("ABCD1234", "모락"), new TeamResponse("1234ABCD", "모락오라"))
        );

        // when & then
        mockMvc.perform(
                get("/api/groups")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                document(
                        "groups",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @Test
    void 그룹에_속한_멤버_목록을_조회한다() throws Exception {
        // given
        given(teamService.findMembersInTeam(anyLong(), anyString())).willReturn(
                List.of(new MemberResponse(1L, "배카라", "https://github.com/seong-wooo"),
                        new MemberResponse(2L, "차리", "https://github.com/cjlee"))
        );

        // when & then
        mockMvc.perform(
                get("/api/groups/{group-code}/members", "ABCD1234")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                document(
                        "group-members",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("group-code").description("그룹 코드")
                        )

                )
        );
    }

    @Test
    void 그룹에서_탈퇴한다() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/api/groups/out/{group-code}", "ABCD1234")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(
                document(
                        "group-out",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("group-code").description("그룹 코드")
                        )
                )
        );
    }

    @Test
    void 기본_그룹을_조회한다() throws Exception {
        // when & then
        mockMvc.perform(
                get("/api/groups/default")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                document(
                        "group-default",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }
}