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
                        Poll.builder()
                                .id(rs.getLong("id"))
                                .team(supplyTeam(rs.getLong("team_id")))
                                .host(supplyMember(rs.getLong("host_id")))
                                .title(rs.getString("title"))
                                .allowedPollCount(rs.getInt("allowed_poll_count"))
                                .isAnonymous(rs.getBoolean("is_anonymous"))
                                .status(PollStatus.valueOf(rs.getString("status")))
                                .closedAt(rs.getTimestamp("closed_at").toLocalDateTime())
                                .code(rs.getString("code"))
                                .build()
                , id);
    }

    public PollItem supplyPollItem(Long id) {
        String sql = "SELECT * FROM poll_item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                PollItem.builder()
                        .id(rs.getLong("id"))
                        .poll(supplyPoll(rs.getLong("poll_id")))
                        .subject(rs.getString("subject"))
                        .build(), id);
    }
}
