package com.morak.back.role.domain;

import com.morak.back.core.domain.Code;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RoleEntityRepositoryImpl implements RoleEntityRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoleEntityRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Role> findWithOnlyHistoriesPerDate(String teamCode) {
        Role role = queryRole(teamCode);
        if (role == null) {
            return Optional.empty();
        }
        RoleHistories roleHistories = findRoleHistories(role.getId());
        Role roleWithHistories = new Role(teamCode, null, roleHistories);
        return Optional.of(roleWithHistories);
    }

    private Role queryRole(String teamCode) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM role r WHERE r.team_code = :teamCode",
                    new MapSqlParameterSource("teamCode", teamCode),
                    (rs, rowNum) -> new Role(rs.getLong("id"), Code.generate(ignored -> teamCode), null, null)
            );
        } catch (RuntimeException e) {
            return null;
        }
    }

    private RoleHistories findRoleHistories(Long roleId) {
        List<RoleHistory> roleHistories = queryRoleHistories(roleId);
        if (roleHistories.isEmpty()) {
            return new RoleHistories(roleHistories);
        }
        Map<Long, RoleHistory> IdsByHistory = roleHistories.stream()
                .collect(Collectors.toMap(RoleHistory::getId, Function.identity()));
        addMatchResults(IdsByHistory);
        return new RoleHistories(roleHistories);
    }

    private List<RoleHistory> queryRoleHistories(Long roleId) {
        return jdbcTemplate.query(
                "SELECT MAX(ID) id, MAX(rh.date_time) date_time FROM role_history rh WHERE rh.role_id = :roleId GROUP BY cast(rh.date_time as DATE)",
                new MapSqlParameterSource("roleId", roleId),
                (rs, rowNum) -> new RoleHistory(
                        rs.getLong("id"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        new ArrayList<>()
                )
        );
    }

    private void addMatchResults(Map<Long, RoleHistory> idsByHistory) {
        jdbcTemplate.query(
                "SELECT * FROM role_match_result rms WHERE rms.role_history_id in (:ids)",
                new MapSqlParameterSource("ids", idsByHistory.keySet()),
                (rs, rowNum) -> {
                    RoleMatchResult matchResult = new RoleMatchResult(
                            new RoleName(rs.getString("role_name")),
                            rs.getLong("member_id")
                    );
                    RoleHistory history = idsByHistory.get(rs.getLong("role_history_id"));
                    history.getMatchResults().add(matchResult);
                    return null;
                });
    }
}
