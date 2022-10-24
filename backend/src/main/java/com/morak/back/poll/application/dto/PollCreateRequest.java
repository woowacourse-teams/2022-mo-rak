package com.morak.back.poll.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morak.back.appointment.domain.menu.ClosedAt;
import com.morak.back.appointment.domain.menu.Menu;
import com.morak.back.appointment.domain.menu.MenuStatus;
import com.morak.back.appointment.domain.menu.Title;
import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.domain.SystemDateTime;
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

//    public Poll toPoll(Long teamId, Long hostId) {
//        Menu menu = Menu.builder()
//                .code(Code.generate(new RandomCodeGenerator()))
//                .title(new Title(title))
//                .teamId(teamId)
//                .hostId(hostId)
//                .status(PollStatus.OPEN)
//                .closedAt(new SystemDateTime(closedAt))
//                .build();
//        List<PollItem> pollItems = subjects.stream()
//                .map(subject -> PollItem.builder().subject(subject).build())
//                .collect(Collectors.toList());
//        return Poll.builder()
//                .menu(pollInfo)
//                .pollItems(pollItems)
//                .anonymous(anonymous)
//                .allowedCount(allowedPollCount)
//                .build();
//    }

    public Poll toPoll(String teamCode, Long hostId) {
        Menu menu = Menu.builder()
                .teamCode(Code.generate((s) -> teamCode))
                .hostId(hostId)
                .code(Code.generate(new RandomCodeGenerator()))
                .title(new Title(title))
                .status(MenuStatus.OPEN)
                .closedAt(new ClosedAt(closedAt, LocalDateTime.now()))
                .build();
        List<PollItem> pollItems = subjects.stream()
                .map(subject -> PollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        return Poll.builder()
                .menu(menu)
                .pollItems(pollItems)
                .anonymous(anonymous)
                .allowedCount(allowedPollCount)
                .build();
    }
}
