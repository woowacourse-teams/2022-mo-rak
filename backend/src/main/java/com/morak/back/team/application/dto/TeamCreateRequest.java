package com.morak.back.team.application.dto;

import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.team.domain.Team;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateRequest {

    @NotBlank(message = "team name은 blank 일 수 없습니다")
    private String name;

    public Team toTeam(final CodeGenerator codeGenerator) {
        return Team.builder()
                .name(this.name)
                .code(Code.generate(codeGenerator))
                .build();
    }
}
