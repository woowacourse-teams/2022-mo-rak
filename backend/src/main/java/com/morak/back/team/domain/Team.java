package com.morak.back.team.domain;

import com.morak.back.core.domain.Code;
import com.morak.back.core.support.Generated;
import com.morak.back.poll.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "그룹 이름은 빈 값일 수 없습니다.")
    private String name;

    @Embedded
    @Valid
    private Code code;

    @Builder
    private Team(Long id, String name, Code code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code.getCode();
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
