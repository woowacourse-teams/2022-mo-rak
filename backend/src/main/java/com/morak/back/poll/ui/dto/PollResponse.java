package com.morak.back.poll.ui.dto;

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

    private LocalDateTime createdAt;

    private LocalDateTime closedAt;

    private String code;

    private Boolean isHost;

    private Integer count;

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
                poll.isHost(member),
                poll.getCount()
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
