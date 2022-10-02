package com.morak.back.brandnew;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.brandnew.domain.Member;
import com.morak.back.brandnew.domain.PollManager;
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

    private int allowedPollCount;

    private Boolean isAnonymous;

    private boolean closed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    private String code;

    private Boolean isHost;

    private Integer count;

    public static PollResponse from(Member member, PollManager pollManager) {
        return new PollResponse(
                pollManager.getId(),
                pollManager.getPoll().getTitle(),
                pollManager.getPoll().getAllowedCount(),
                pollManager.getPoll().getAnonymous(),
                pollManager.getPoll().getClosed(),
                pollManager.getPoll().getClosedAt().getDateTime(),
                pollManager.getPoll().getCode().getCode(),
                pollManager.getPoll().isHost(member),
                pollManager.countSelectMembers()
        );
    }
}
