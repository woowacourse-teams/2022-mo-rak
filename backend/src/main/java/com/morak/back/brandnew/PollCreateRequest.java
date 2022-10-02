package com.morak.back.brandnew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.brandnew.domain.Member;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.domain.Poll;
import com.morak.back.brandnew.domain.PollManager;
import com.morak.back.brandnew.domain.TempDateTime;
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


    public PollManager toPollManager(Member host) {
        Poll poll = Poll.builder()
                .title(title)
                .anonymous(anonymous)
                .allowedCount(allowedPollCount)
                .host(host)
                .closed(false)
                .closedAt(new TempDateTime(closedAt))
                .build();
        List<NewPollItem> pollItems = subjects.stream()
                .map(subject -> NewPollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        return PollManager.builder()
                .poll(poll)
                .pollItems(pollItems)
                .build();
    }
}
