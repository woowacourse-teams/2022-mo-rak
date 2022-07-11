package com.morak.back.poll.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PollItemRequest {

    @NotEmpty
    private List<Long> itemIds;

    @JsonCreator
    public PollItemRequest(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
