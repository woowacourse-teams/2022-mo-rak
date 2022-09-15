package com.morak.back.performance.dao;

import com.morak.back.auth.domain.Member;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class TeamMemberDao {

    private final JdbcTemplate jdbcTemplate;

    public TeamMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertMembers(List<Member> members) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) VALUES (?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, members.get(i).getOauthId());
                        ps.setString(2, members.get(i).getName());
                        ps.setString(3, members.get(i).getProfileUrl());
                    }

                    @Override
                    public int getBatchSize() {
                        return members.size();
                    }
                }
        );
    }

    public void batchInsertTeams(List<Team> teams) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team (name, code, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, teams.get(i).getName());
                        ps.setString(2, teams.get(i).getCode());
                    }

                    @Override
                    public int getBatchSize() {
                        return teams.size();
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

    public void batchInsertTeamMember(List<TeamMember> teamMembers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_member (team_id, member_id, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, teamMembers.get(i).getTeam().getId());
                        ps.setLong(2, teamMembers.get(i).getMember().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return teamMembers.size();
                    }
                }
        );
    }
}
