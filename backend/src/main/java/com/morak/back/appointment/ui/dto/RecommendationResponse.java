package com.morak.back.appointment.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.auth.ui.dto.MemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    private Integer rank;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime recommendStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mma", locale = "en_US")
    private LocalDateTime recommendEndDateTime;

    private List<MemberResponse> availableMembers;

    private List<MemberResponse> unavailableMembers;
}
