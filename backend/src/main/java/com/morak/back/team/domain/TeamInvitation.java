package com.morak.back.team.domain;

import com.morak.back.poll.domain.BaseEntity;
import java.time.LocalDateTime;
import java.util.function.Function;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamInvitation extends BaseEntity {

    private static final long EXPIRED_MINUTES = 30L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Embedded
    private InvitationCode code;

    @Embedded
    private ExpiredTime expiredAt;

    public static TeamInvitation issue(Team team, Function<Integer, String> codeGenerator) {
        return new TeamInvitation(null, team, InvitationCode.generate(codeGenerator), ExpiredTime.withMinute(EXPIRED_MINUTES));
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public String getCode() {
        return code.getCode();
    }
}
