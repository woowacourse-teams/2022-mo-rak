package com.morak.back.poll.application.dto;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollInfo;
import com.morak.back.poll.domain.SystemDateTime;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.domain.Team;
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

    // TODO: 2022/10/13 anonymous  로 바꾸기
    @NotNull(message = "isAnonymous 는 null 일 수 없습니다.")
    private Boolean isAnonymous;

    @Future(message = "closedAt은 미래여야 합니다.")
    private LocalDateTime closedAt;

    @NotNull(message = "subjects 는 null 일 수 없습니다.")
    private List<String> subjects;

    public Poll toPoll(Long teamId, Long hostId) {
        PollInfo pollInfo = PollInfo.builder()
                .codeGenerator(new RandomCodeGenerator())
                .title(title)
                .anonymous(isAnonymous)
                .allowedCount(allowedPollCount)
                .teamId(teamId)
                .hostId(hostId)
                .status(PollStatus.OPEN)
                .closedAt(new SystemDateTime(closedAt))
                .build();
        List<PollItem> pollItems = subjects.stream()
                .map(subject -> PollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        return Poll.builder()
                .pollInfo(pollInfo)
                .pollItems(pollItems)
                .build();
    }
}
