package com.morak.back.auth.ui.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeNameRequest {

    @NotNull(message = "변경하려는 이름은 null 수 없일니다.")
    private String name;
}
