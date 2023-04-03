package com.morak.back.role.domain;

import static com.morak.back.role.domain.QRole.role;
import static com.morak.back.role.domain.QRoleHistory.roleHistory;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
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
        Long roleId = queryRoleId(teamCode);
        if (roleId == null) {
            return Optional.empty();
        }
        return Optional.of(findRoleWithGroupByAndOrderBy(teamCode, roleId));
    }

    private Long queryRoleId(String teamCode) {
        return jpaQueryFactory.select(role.id)
                .from(role)
                .where(role.teamCode.code.eq(teamCode))
                .fetchOne();
    }

    private Role findRoleWithGroupByAndOrderBy(String teamCode, Long roleId) {
        List<Long> roleHistoryIds = findRoleHistoryIdsGroupByAndOrderBy(roleId);
        List<RoleHistory> roleHistories = findAllRoleHistory(roleHistoryIds);
        return new Role(teamCode, null, new RoleHistories(roleHistories));
    }

    private List<Long> findRoleHistoryIdsGroupByAndOrderBy(Long roleId) {
        return jpaQueryFactory.select(roleHistory.id.max())
                .from(roleHistory)
                .groupBy(roleHistory.date)
                .orderBy(roleHistory.date.desc())
                .where(roleHistory.roleId.eq(roleId))
                .fetch();
    }

    private List<RoleHistory> findAllRoleHistory(List<Long> ids) {
        return jpaQueryFactory.selectFrom(roleHistory)
                .from(roleHistory)
                .where(roleHistory.id.in(ids))
                .fetch();
    }
}
