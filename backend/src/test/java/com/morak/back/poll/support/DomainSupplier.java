package com.morak.back.poll.support;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.team.domain.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainSupplier {

    private final JdbcTemplate jdbcTemplate;

    public DomainSupplier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member supplyMember(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                Member.builder()
                        .id(rs.getLong("id"))
                        .oauthId(rs.getString("oauth_id"))
                        .name(rs.getString("name"))
                        .profileUrl(rs.getString("profile_url")).build(), id);
    }

    public Team supplyTeam(Long id) {
        String sql = "SELECT * FROM team WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Team(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("code")
                ), id);
    }

    public Poll supplyPoll(Long id) {
        String sql = "SELECT * FROM poll WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Poll(rs.getLong("id"),
                        supplyTeam(rs.getLong("team_id")),
                        supplyMember(rs.getLong("host_id")),
                        rs.getString("title"),
                        rs.getInt("allowed_poll_count"),
                        rs.getBoolean("is_anonymous"),
                        PollStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("closed_at").toLocalDateTime(),
                        rs.getString("code")
                ), id);
    }

    public PollItem supplyPollItem(Long id) {
        String sql = "SELECT * FROM poll_item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new PollItem(rs.getLong("id"),
                        supplyPoll(rs.getLong("poll_id")),
                        rs.getString("subject")
                ), id);
    }
}
