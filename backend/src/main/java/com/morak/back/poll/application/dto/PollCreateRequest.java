package com.morak.back.poll.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.core.domain.menu.ClosedAt;
import com.morak.back.core.domain.menu.MenuStatus;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollCreateRequest {

    @NotBlank(message = "title은 blank일 수 없습니다.")
    private String title;

    @NotNull(message = "allowedPollCount 는 null 일 수 없습니다.")
    private Integer allowedPollCount;

    @NotNull(message = "isAnonymous 는 null 일 수 없습니다.")
    @JsonProperty("isAnonymous")
    private Boolean anonymous;

    @Future(message = "closedAt은 미래여야 합니다.")
    private LocalDateTime closedAt;

    @NotNull(message = "subjects 는 null 일 수 없습니다.")
    private List<String> subjects;

    public Poll toPoll(String teamCode, Long hostId) {
        List<PollItem> pollItems = subjects.stream()
                .map(subject -> PollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        return Poll.builder()
                .teamCode(Code.generate((s) -> teamCode))
                .hostId(hostId)
                .code(Code.generate(new RandomCodeGenerator()))
                .title(title)
                .status(MenuStatus.OPEN)
                .closedAt(new ClosedAt(closedAt, LocalDateTime.now()))
                .pollItems(pollItems)
                .anonymous(anonymous)
                .allowedCount(allowedPollCount)
                .build();
    }
}
