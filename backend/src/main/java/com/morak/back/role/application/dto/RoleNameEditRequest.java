package com.morak.back.role.application.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleNameEditRequest {

    @NotNull(message = "roles는 null 일 수 없습니다.")
    private List<String> roles;
}
