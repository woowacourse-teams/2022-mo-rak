package com.morak.back.poll.application.dto;

import com.morak.back.poll.domain.PollItem;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PollResultRequest {

    @NotNull(message = "ItemId는 null 일 수 없습니다.")
    private Long id;

    @NotNull(message = "poll Item description은 null 일 수 없습니다.")
    private String description;

    public PollItem toPollItem() {
        return new PollItem(id);
    }
}
