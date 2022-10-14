package com.morak.back.role.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.poll.ui.ControllerTest;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.application.dto.RoleNameResponses;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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



    // -- C



    // -- D


}
