package com.morak.back.team.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TeamMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public TeamMember(final Long id, final Team team, final Member member) {
        super(id);
        this.team = team;
        this.member = member;
    }
}
