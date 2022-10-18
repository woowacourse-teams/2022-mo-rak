package com.morak.back.auth.ui.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeNameRequest {

    @NotBlank(message = "변경하려는 이름은 blank일 수 없습니다.")
    private String name;
}
