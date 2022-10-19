package com.morak.back.team.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TeamMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "team 은 null 일 수 없습니다.")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "member 는 null 일 수 없습니다")
    private Member member;

    @Builder
    public TeamMember(final Long id, final Team team, final Member member) {
        super(id);
        this.team = team;
        this.member = member;
    }
}
