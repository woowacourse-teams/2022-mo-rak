package com.morak.back.team.domain;

import com.morak.back.core.domain.BaseEntity;
import com.morak.back.core.domain.Code;
import com.morak.back.core.support.Generated;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Team extends BaseEntity {

    @Embedded
    private Name name;

    @Embedded
    private Code code;

    @Builder
    private Team(Long id, String name, Code code) {
        this(id, new Name(name), code);
    }

    private Team(Long id, Name name, Code code) {
        super(id);
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code.getCode();
    }

    public String getName() {
        return name.getName();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Team team = (Team) o;
        return Objects.equals(getId(), team.getId()) && Objects.equals(getName(), team.getName())
                && Objects.equals(getCode(), team.getCode());
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCode());
    }
}
