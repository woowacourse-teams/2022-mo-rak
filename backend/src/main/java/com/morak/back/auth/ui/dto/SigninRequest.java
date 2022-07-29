package com.morak.back.auth.ui.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

    @NotBlank(message = "code는 blank일 수 없습니다.")
    private String code;
}
