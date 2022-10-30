package com.morak.back.performance.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("performance")
public class RoleDao {

    private final JdbcTemplate jdbcTemplate;

    public RoleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertRoles(List<String> teamCodes) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO role (team_code, created_at, updated_at) VALUES (?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, teamCodes.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return teamCodes.size();
                    }
                }
        );
    }

    public void batchInsertRoleNames(List<Entry<Long, String>> roleNames) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO role_name (role_id, name) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Entry<Long, String> roleName = roleNames.get(i);
                        ps.setLong(1, roleName.getKey());
                        ps.setString(2, roleName.getValue());
                    }

                    @Override
                    public int getBatchSize() {
                        return roleNames.size();
                    }
                }
        );
    }

    public void batchInsertRoleHistories(long roleId, List<LocalDateTime> dateTimes) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO role_history (date_time, role_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, dateTimes.get(i));
                        ps.setLong(2, roleId);
                        batchInsertRoleMatchResult(i);
                    }

                    @Override
                    public int getBatchSize() {
                        return dateTimes.size();
                    }
                }
        );
    }

    private void batchInsertRoleMatchResult(long roleHistoryId) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO role_match_result (role_history_id, member_id, role_name) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, roleHistoryId);
                        ps.setLong(2, i);
                        ps.setString(3, "데일리 마스터");
                    }

                    @Override
                    public int getBatchSize() {
                        return 3;
                    }
                }
        );
    }
}
