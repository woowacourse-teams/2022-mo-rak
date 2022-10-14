package com.morak.back.role.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.poll.ui.ControllerTest;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import com.morak.back.role.application.dto.RoleNameResponses;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(RoleController.class)
class RoleControllerTest extends ControllerTest {

    @MockBean
    private RoleService roleService;

    private final String groupCode = "rlgHKPj3";
    // -- A

    @Test
    void 역할_이름_목록을_조회한다() throws Exception {
        RoleNameResponses roleNameResponses = new RoleNameResponses(List.of("반장", "청소부"));
        given(roleService.findRoleNames(anyString(), anyLong())).willReturn(roleNameResponses);

        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/roles/names", groupCode)
                .header("Authorization", "bearer.access.token"));

        response
                .andExpect(status().isOk())
                .andDo(document("role/get-role-names",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupCode").description("그룹_코드"))
                ));
    }

    // -- B

    @Test
    void 역할정하기_이름_목록을_변경한다() throws Exception {
        // given
        RoleNameEditRequest request = new RoleNameEditRequest(List.of("서기", "타임키퍼", "공석"));
        doNothing().when(roleService).editRoleNames(anyString(), anyLong(), any());

        // when
        ResultActions response = mockMvc.perform(put("/api/groups/{groupCode}/roles/names", "AB1234CD")
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        response.andExpect(status().isOk())
                .andDo(document("role/role-name-edit",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupCode").description("그룹_코드"))
                ));
    }

    // -- C
    @Test
    void 역할을_정한다() throws Exception {
        // given
        String groupCode = "rlgHKPj3";
        Long roleId = 1L;
        given(roleService.match(anyString(), anyLong())).willReturn(roleId);

        // when
        ResultActions response = mockMvc.perform(post("/api/groups/{groupCode}/roles", groupCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/groups/" + groupCode + "/roles/" + roleId))
                .andDo(document("role/role-match",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupCode").description("그룹_코드"))
                ));
    }

    // -- D


}
