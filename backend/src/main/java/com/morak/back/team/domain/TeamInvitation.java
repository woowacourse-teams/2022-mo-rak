package com.morak.back.team.domain;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.core.domain.BaseEntity;
import com.morak.back.core.domain.Code;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TeamInvitation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Embedded
    private Code code;

    @Embedded
    private ExpiredTime expiredAt;

    @Builder
    private TeamInvitation(Long id, Team team, Code code, ExpiredTime expiredAt) {
        super(id);
        this.team = team;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired(SystemTime systemTime) {
        return expiredAt.isBefore(systemTime.now());
    }

    public String getCode() {
        return code.getCode();
    }
}
