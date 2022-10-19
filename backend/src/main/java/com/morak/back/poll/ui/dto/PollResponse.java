package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse implements Comparable<PollResponse> {

    private Long id;

    private String title;

    private Integer allowedPollCount;

    private Boolean isAnonymous;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    private String code;

    private Boolean isHost;

    private Integer count;

    public static PollResponse from(Long memberId, Poll poll) {
        return new PollResponse(
                poll.getId(),
                poll.getPollInfo().getTitle(),
                poll.getPollInfo().getAllowedCount().getValue(),
                poll.getPollInfo().getAnonymous(),
                poll.getPollInfo().getStatus().name(),
                poll.getCreatedAt(),
                poll.getPollInfo().getClosedAt().getDateTime(),
                poll.getPollInfo().getCode(),
                poll.getPollInfo().isHost(memberId),
                poll.countSelectMembers()
        );
    }

    @Override
    public int compareTo(PollResponse o) {
        if (this.status.equalsIgnoreCase(o.status)) {
            return Long.compare(o.id, this.id);
        }
        if (PollStatus.OPEN.name().equalsIgnoreCase(this.status)) {
            return -1;
        }
        return 1;
    }
}
