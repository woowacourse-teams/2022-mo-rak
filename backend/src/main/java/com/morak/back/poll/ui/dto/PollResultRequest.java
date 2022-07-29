package com.morak.back.poll.ui.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PollResultRequest {

    @NotNull(message = "ItemId는 null 일 수 없습니다.")
    private Long itemId;

    @NotNull(message = "poll Item description은 null 일 수 없습니다.")
    private String description;
}
