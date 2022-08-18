package com.morak.back.core.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.ui.ControllerTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(GlobalController.class)
public class ErrorCodesControllerTest extends ControllerTest {

    @Test
    void 에러_코드_문서화_작성() throws Exception {
        ResultActions response = mockMvc.perform(get("/error-codes"));

        response.andDo(
                document("error-codes",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                create()
                        )
                )
        );
    }

    private List<FieldDescriptor> create() {
        return Arrays.stream(CustomErrorCode.values())
                .map(code -> fieldWithPath("code").description(code.getInformation()).type(code.getNumber()))
                .collect(Collectors.toList());
    }
}
