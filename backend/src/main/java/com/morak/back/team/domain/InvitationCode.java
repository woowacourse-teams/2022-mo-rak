package com.morak.back.team.domain;

import java.util.function.Function;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCode {

    private static final int CODE_LENGTH = 10;

    private String code;

    public static InvitationCode generate(Function<Integer, String> codeGenerator) {
        return new InvitationCode(codeGenerator.apply(CODE_LENGTH));
    }
}
