package com.morak.back.team.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.team.exception.TeamDomainLogicException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Name {

    private static final int MAX_LENGTH = 255;

    private String name;

    public Name(String name) {
        validateLength(name);
        this.name = name;
    }

    private void validateLength(String name) {
        if (name.isBlank() || name.length() > MAX_LENGTH) {
            throw new TeamDomainLogicException(
                    CustomErrorCode.TEAM_NAME_ERROR,
                    "팀 이름" + name + "이 너무 길거나 공백일 수 없습니다."
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
