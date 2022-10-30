package com.morak.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public class SimpleMockMvc {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static ResultActions get(MockMvc mockMvc, String path, String groupCode) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get(path, groupCode)
                .header("Authorization", "bearer access.token"));
    }

    public static ResultActions get(MockMvc mockMvc, String path, String groupCode, String menuCode) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get(path, groupCode, menuCode)
                .header("Authorization", "bearer access.token"));
    }

    public static ResultActions post(MockMvc mockMvc, String path, String groupCode, Object request) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(path, groupCode)
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    public static ResultActions put(MockMvc mockMvc, String path, String groupCode, String menuCode, Object request)
            throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.put(path, groupCode, menuCode)
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    public static ResultActions patch(MockMvc mockMvc, String path, String groupCode, String menuCode)
            throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.patch(path, groupCode, menuCode)
                .header("Authorization", "bearer access.token"));
    }

    public static ResultActions delete(MockMvc mockMvc, String path, String groupCode, String menuCode)
            throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.delete(path, groupCode, menuCode)
                .header("Authorization", "bearer access.token"));
    }
}
