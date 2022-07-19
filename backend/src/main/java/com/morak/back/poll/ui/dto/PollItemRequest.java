package com.morak.back.poll.ui.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PollItemRequest {

    @NotNull
    private Long itemId;

    @NotNull
    private String description;
}
