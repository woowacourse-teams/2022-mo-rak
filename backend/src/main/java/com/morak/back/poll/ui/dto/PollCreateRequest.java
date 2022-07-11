package com.morak.back.poll.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.auth.domain.Team;

import lombok.Getter;

@Getter
public class PollCreateRequest {

    @NotBlank
    private final String title;

    @Min(1)
    @NotNull
    private final Integer allowedPollCount;

    @NotNull
    private final Boolean isAnonymous;

    @NotNull
    private final LocalDateTime closedAt;

    @NotNull
    @Size(min = 2)
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

        return new Poll(null, team, member, title, allowedPollCount, isAnonymous, status, closedAt, code);
    }

    public List<PollItem> toPollItems(Poll poll) {
        return subjects.stream()
            .map(subject -> new PollItem(null, poll, subject))
            .collect(Collectors.toList());
    }
}
