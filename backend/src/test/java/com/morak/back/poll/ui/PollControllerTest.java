package com.morak.back.poll.ui;

import static com.morak.back.ApiDocumentUtils.getDocumentRequest;
import static com.morak.back.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.morak.back.poll.application.PollService;
import com.morak.back.poll.application.dto.MemberResultResponse;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(PollController.class)
class PollControllerTest extends ControllerTest {

    @MockBean
    private PollService pollService;

    private final String groupCode = "rlgHKPj3";
    private final String pollCode = "abCD1234";

    @Test
    void 투표를_생성한다() throws Exception {
        // given
        PollCreateRequest pollCreateRequest = new PollCreateRequest("회식_메뉴", 2, false, LocalDateTime.now().plusDays(1),
                List.of("회", "삼겹살", "꿔바로우"));

        PollResponse pollResponse = new PollResponse(1L, "회식_메뉴", 2, false, "OPEN", LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(3), pollCode, true, 0);
        given(pollService.createPoll(anyString(), anyLong(), any(PollCreateRequest.class))).willReturn(pollResponse);

        // when
        ResultActions response = mockMvc.perform(post("/api/groups/{groupCode}/polls", groupCode)
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pollCreateRequest)));

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/groups/" + groupCode + "/polls/" + pollCode))
                .andDo(document("poll/poll-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupCode").description("그룹_코드"))
                ));
    }

    @Test
    void 투표를_진행한다() throws Exception {
        // given
        List<PollResultRequest> pollResultRequests = List.of(
                new PollResultRequest(1L, "회는_싱싱하니까요~"),
                new PollResultRequest(2L, "삼겹살은_언제나_좋아요!!")
        );

        // when
        ResultActions response = mockMvc.perform(put("/api/groups/{groupCode}/polls/{pollCode}", groupCode, pollCode)
                .header("Authorization", "bearer access.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pollResultRequests)));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/do-poll",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )
                ));
    }

    @Test
    void 투표_목록을_조회한다() throws Exception {
        // given
        given(pollService.findPolls(anyString(), anyLong()))
                .willReturn(List.of(
                        new PollResponse(1L, "회식_메뉴", 2, false, "OPEN", LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(3), groupCode, true, 0),
                        new PollResponse(2L, "좋아하는_색상", 1, true, "OPEN", LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(3), "SZ72Yofx", false, 0)
                ));

        // when
        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/polls", groupCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/get-polls",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드")
                        )
                ));
    }

    @Test
    void 투표를_조회한다() throws Exception {
        // given
        given(pollService.findPoll(anyString(), anyLong(), anyString()))
                .willReturn(new PollResponse(1L, "회식_메뉴", 2, false, "OPEN", LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(3), pollCode, true, 0));

        // when
        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/polls/{pollCode}", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/get-poll",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )));
    }

    @Test
    void 투표_선택_항목들을_조회한다() throws Exception {
        // given
        given(pollService.findPollItems(anyString(), anyLong(), anyString()))
                .willReturn(List.of(
                        new PollItemResponse(1L, "회", true, "위니가_회를_참_좋아해요."),
                        new PollItemResponse(2L, "삼겹살", true, "해리가_정말_좋아해요."),
                        new PollItemResponse(3L, "꿔바로우", false, "")
                ));

        // when
        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/polls/{pollCode}/items", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/get-poll-items",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )));
    }

    @Test
    void 익명_투표_결과를_조회한다() throws Exception {
        // given
        MemberResultResponse memberResultResponse1 = new MemberResultResponse(0L, "", "", "위니가_회를_참_좋아해요.");
        MemberResultResponse memberResultResponse2 = new MemberResultResponse(0L, "", "", "해리가_삼겹살을_정말_좋아해요.");

        given(pollService.findPollResults(anyString(), anyLong(), anyString()))
                .willReturn(List.of(
                        new PollItemResultResponse(1L, 1, List.of(memberResultResponse1), "회"),
                        new PollItemResultResponse(2L, 1, List.of(memberResultResponse2), "삼겹살"),
                        new PollItemResultResponse(3L, 0, List.of(), "꿔바로우")
                ));

        // when
        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/polls/{pollCode}/result", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/get-poll-results-anonymous",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )
                ));
    }

    @Test
    void 기명_투표_결과를_조회한다() throws Exception {
        // given
        MemberResultResponse memberResultResponse1 = new MemberResultResponse(1L, "송상민씨", "albur-profile-image-url",
                "위니가_회를_참_좋아해요.");
        MemberResultResponse memberResultResponse2 = new MemberResultResponse(2L, "리엘", "ellie-profile-image-url",
                "해리가_삼겹살을_정말_좋아해요.");

        given(pollService.findPollResults(anyString(), anyLong(), anyString()))
                .willReturn(List.of(
                        new PollItemResultResponse(1L, 1, List.of(memberResultResponse1), "회"),
                        new PollItemResultResponse(2L, 1, List.of(memberResultResponse2), "삼겹살"),
                        new PollItemResultResponse(3L, 0, List.of(), "꿔바로우")
                ));

        // when
        ResultActions response = mockMvc.perform(get("/api/groups/{groupCode}/polls/{pollCode}/result", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/get-poll-results",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )
                ));
    }

    @Test
    void 투표를_삭제한다() throws Exception {
        // when
        ResultActions response = mockMvc.perform(delete("/api/groups/{groupCode}/polls/{pollCode}", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isNoContent())
                .andDo(document("poll/delete-poll",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )));
    }

    @Test
    void 투표를_마감한다() throws Exception {
        // when
        ResultActions response = mockMvc.perform(patch("/api/groups/{groupCode}/polls/{pollCode}/close", groupCode, pollCode)
                .header("Authorization", "bearer access.token"));

        // then
        response
                .andExpect(status().isOk())
                .andDo(document("poll/close-poll",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("groupCode").description("그룹_코드"),
                                parameterWithName("pollCode").description("투표_코드")
                        )));
    }
}
