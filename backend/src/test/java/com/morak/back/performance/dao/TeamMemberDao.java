package com.morak.back.performance.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("performance")
public class TeamMemberDao {

    private final JdbcTemplate jdbcTemplate;

    public TeamMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertMembers(int memberSize) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) VALUES (?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, "oauth-id" + (i + 1));
                        ps.setString(2,"더미 멤버");
                        ps.setString(3, "더미 멤버 프로필");
                    }

                    @Override
                    public int getBatchSize() {
                        return memberSize;
                    }
                }
        );
    }

    public void batchInsertTeams(int teamSize) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team (name, code, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, "더미 팀");
                        ps.setString(2, String.format("%08d", i + 1));
                    }

                    @Override
                    public int getBatchSize() {
                        return teamSize;
                    }
                }
        );
    }

    public void batchInsertTeamMembersMain(int size) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_member (team_id, member_id, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, i + 1);
                        ps.setLong(2, 1);
                    }

                    @Override
                    public int getBatchSize() {
                        return size;
                    }
                }
        );
    }

    public void batchInsertTeamMember(List<Entry<Long, Long>> teamMembers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_member (team_id, member_id, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, teamMembers.get(i).getKey());
                        ps.setLong(2, teamMembers.get(i).getValue());
                    }

                    @Override
                    public int getBatchSize() {
                        return teamMembers.size();
                    }
                }
        );
    }
}
