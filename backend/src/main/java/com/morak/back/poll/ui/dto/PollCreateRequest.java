package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PollCreateRequest {

    @NotBlank
    private final String title;

    @NotNull
    private final Integer allowedPollCount;

    @NotNull
    private final Boolean isAnonymous;

    @NotNull
    private final LocalDateTime closedAt;

    @NotNull
    private final List<String> subjects;

    @JsonCreator
    public PollCreateRequest(String title, Integer allowedPollCount, Boolean isAnonymous, LocalDateTime closedAt,
                             List<String> subjects) {
        this.title = title;
        this.allowedPollCount = allowedPollCount;
        this.isAnonymous = isAnonymous;
        this.closedAt = closedAt;
        this.subjects = subjects;
    }

    public Poll toPoll(Member member, Team team, PollStatus status, String code) {
        return Poll.builder()
                .team(team)
                .host(member)
                .title(title)
                .allowedPollCount(allowedPollCount)
                .isAnonymous(isAnonymous)
                .status(status)
                .closedAt(closedAt)
                .code(code)
                .build();
    }

    public List<PollItem> toPollItems(Poll poll) {
        return subjects.stream()
                .map(
                        subject -> PollItem.builder()
                                .poll(poll)
                                .subject(subject)
                                .build()
                )
                .collect(Collectors.toList());
    }
}
