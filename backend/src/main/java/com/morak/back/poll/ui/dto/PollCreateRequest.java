package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollCreateRequest {

    @NotBlank(message = "title은 blank일 수 없습니다.")
    private String title;

    @NotNull(message = "allowedPollCount 는 null 일 수 없습니다.")
    private Integer allowedPollCount;

    @NotNull(message = "isAnonymous 는 null 일 수 없습니다.")
    private Boolean isAnonymous;

    @Future(message = "closedAt은 미래여야 합니다.")
    private LocalDateTime closedAt;

    @NotNull(message = "subjects 는 null 일 수 없습니다.")
    private List<String> subjects;

    public Poll toPoll(Member member, Team team, PollStatus status, Code code) {
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
