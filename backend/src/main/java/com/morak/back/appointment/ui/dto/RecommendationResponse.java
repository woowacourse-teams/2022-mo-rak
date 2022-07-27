package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.auth.ui.dto.MemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationResponse {

    private final Integer rank;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime recommendStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime recommendEndDateTime;

    // TODO: 2022/07/27 기존의 MemberResponse를 쓰기 위해 memberId를 id로 변경해서 사용하고 있음!
    private final List<MemberResponse> availableMembers;

    private final List<MemberResponse> unavailableMembers;
}
