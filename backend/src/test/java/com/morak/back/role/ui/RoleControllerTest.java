package com.morak.back.role.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.poll.ui.ControllerTest;
import com.morak.back.role.application.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(RoleController.class)
class RoleControllerTest extends ControllerTest {

    @MockBean
    private RoleService roleService;

    // -- A

    // -- B


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
