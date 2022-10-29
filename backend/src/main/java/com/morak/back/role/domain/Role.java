package com.morak.back.role.domain;

import com.morak.back.core.domain.BaseRootEntity;
import com.morak.back.core.domain.Code;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Role extends BaseRootEntity<Role> {

    private static final String DEFAULT_ROLE = "데일리 마스터";

    @AttributeOverride(name = "code", column = @Column(name = "team_code", nullable = false))
    private Code teamCode;

    @Embedded
    private RoleNames roleNames;

    @Embedded
    private RoleHistories roleHistories;

    public Role(Long id, Code teamCode, RoleNames roleNames, RoleHistories roleHistories) {
        super(id);
        this.teamCode = teamCode;
        this.roleNames = roleNames;
        this.roleHistories = roleHistories;
    }

    public Role(String teamCode, RoleNames roleNames, RoleHistories roleHistories) {
        this(null, Code.generate(ignored -> teamCode), roleNames, roleHistories);
    }

    public Role(String teamCode) {
        this(teamCode, RoleNames.from(List.of(DEFAULT_ROLE)), new RoleHistories());
    }

    public void updateNames(List<String> names) {
        this.roleNames = RoleNames.from(names);
    }

    public RoleHistory matchMembers(List<Long> memberIds, ShuffleStrategy strategy) {
        strategy.shuffle(memberIds);
        RoleHistory roleHistory = new RoleHistory(LocalDateTime.now(), roleNames.match(memberIds));
        roleHistories.add(roleHistory);
        registerEvent(RoleHistoryEvent.from(roleHistory, teamCode.getCode()));
        return roleHistory;
    }

    public List<RoleHistory> findAllGroupByDate() {
        return roleHistories.findAllGroupByDate();
    }
}
