package com.morak.back.brandnew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.brandnew.domain.NewPoll;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.domain.PollInfo;
import com.morak.back.brandnew.domain.TempDateTime;
import com.morak.back.poll.domain.PollStatus;
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

    public NewPoll toPoll(String teamCode, Long hostId) {
        PollInfo pollInfo = PollInfo.builder()
                .title(title)
                .anonymous(anonymous)
                .allowedCount(allowedPollCount)
                .teamCode(teamCode)
                .hostId(hostId)
                .status(PollStatus.OPEN)
                .closedAt(new TempDateTime(closedAt))
                .build();
        List<NewPollItem> pollItems = subjects.stream()
                .map(subject -> NewPollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        return NewPoll.builder()
                .pollInfo(pollInfo)
                .pollItems(pollItems)
                .build();
    }
}
