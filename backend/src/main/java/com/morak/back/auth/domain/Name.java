package com.morak.back.auth.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Name {

    @Size(min = 1, max = 255, message = "name의 길이는 1 ~ 255 사이여야합니다.")
    private String name;

    public Name(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
