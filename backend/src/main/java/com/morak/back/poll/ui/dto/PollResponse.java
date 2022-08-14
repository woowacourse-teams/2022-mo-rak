package com.morak.back.poll.ui.dto;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse {

    private Long id;
    private String title;
    private Integer allowedPollCount;
    private Boolean isAnonymous;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private String code;
    private Boolean isHost;

    public static PollResponse from(Poll poll, Member member) {
        return new PollResponse(
                poll.getId(),
                poll.getTitle(),
                poll.getAllowedPollCount(),
                poll.getIsAnonymous(),
                poll.getStatus().name(),
                poll.getCreatedAt(),
                poll.getClosedAt(),
                poll.getCode(),
                poll.isHost(member)
        );
    }
}
