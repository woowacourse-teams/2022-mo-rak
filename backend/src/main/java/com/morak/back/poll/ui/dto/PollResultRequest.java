package com.morak.back.poll.ui.dto;

import com.morak.back.brandnew.domain.NewPollItem;
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

    public NewPollItem toPollItem() {
        return new NewPollItem(id);
    }
}
