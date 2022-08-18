package com.morak.back.team.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.poll.ui.ControllerTest;
import com.morak.back.team.application.TeamService;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TeamController.class)
class TeamControllerTest extends ControllerTest {

    @MockBean
    private TeamService teamService;

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
                                parameterWithName("group-code").description("그룹_코드")
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
                                parameterWithName("invitation-code").description("그룹_초대_코드")
                        )
                )
        );
    }

    @Test
    void 그룹에_가입한다() throws Exception {
        // given
        given(teamService.join(anyLong(), anyString())).willReturn("teamCode");

        // when & then
        mockMvc.perform(
                post("/api/groups/in/{invitation-code}", "asdasd12")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated()
        ).andDo(
                document(
                        "group-joinTeam",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("invitation-code").description("그룹_초대_코드")
                        )
                )
        );
    }

    @Test
    void 그룹_목록을_조회한다() throws Exception {
        // given
        given(teamService.findTeams(anyLong())).willReturn(
                List.of(new TeamResponse(1L, "ABCD1234", "모락"), new TeamResponse(2L, "1234ABCD", "모락오라"))
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
                                parameterWithName("group-code").description("그룹_코드")
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
                                parameterWithName("group-code").description("그룹_코드")
                        )
                )
        );
    }

    @Test
    void 기본_그룹을_조회한다() throws Exception {
        // when & then
        given(teamService.findDefaultTeam(anyLong())).willReturn(
                new TeamResponse(1L, "ABcd1234", "팀 이름")
        );
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
