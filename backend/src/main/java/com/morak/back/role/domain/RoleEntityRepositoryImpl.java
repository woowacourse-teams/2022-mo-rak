package com.morak.back.role.domain;

import static com.morak.back.role.domain.QRole.role;
import static com.morak.back.role.domain.QRoleHistory.roleHistory;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RoleEntityRepositoryImpl implements RoleEntityRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public RoleEntityRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Role> findWithOnlyHistoriesPerDate(String teamCode) {
        RoleHistories roleHistories = findRoleHistories(teamCode);
        Role roleWithHistories = new Role(teamCode, null, roleHistories);
        return Optional.of(roleWithHistories);
    }

    private RoleHistories findRoleHistories(String teamCode) {
        List<RoleHistory> roleHistories = jpaQueryFactory.selectFrom(roleHistory)
                .join(roleHistory.matchResults).fetchJoin()
                .where(roleHistory.id.in(
                        JPAExpressions.select(roleHistory.id.max())
                                .from(roleHistory)
                                .groupBy(toLocalDate())
                                .orderBy(toLocalDate().desc())
                                .where(roleHistory.roleId.eq(
                                        JPAExpressions.select(role.id)
                                                .from(role)
                                                .where(role.teamCode.code.eq(teamCode))
                                ))
                ))
                .fetch();
        return new RoleHistories(roleHistories);
    }

    private DateTemplate<LocalDate> toLocalDate() {
        return Expressions.dateTemplate(
                LocalDate.class,
                "date({0})",
                roleHistory.dateTime);
    }
}
