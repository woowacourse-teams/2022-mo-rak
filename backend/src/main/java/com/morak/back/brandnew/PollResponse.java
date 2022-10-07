package com.morak.back.brandnew;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.brandnew.domain.NewPoll;
import com.morak.back.poll.domain.PollStatus;
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

    private PollStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    private String code;

    private Boolean isHost;

    private Integer count;

    public static PollResponse from(Long memberId, NewPoll poll) {
        return new PollResponse(
                poll.getId(),
                poll.getPollInfo().getTitle(),
                poll.getPollInfo().getAllowedCount(),
                poll.getPollInfo().getAnonymous(),
                poll.getPollInfo().getStatus(),
                poll.getPollInfo().getClosedAt().getDateTime(),
                poll.getPollInfo().getCode(),
                poll.getPollInfo().isHost(memberId),
                poll.countSelectMembers()
        );
    }
}
