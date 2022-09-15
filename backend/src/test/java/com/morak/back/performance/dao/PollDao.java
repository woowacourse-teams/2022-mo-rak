package com.morak.back.performance.dao;

import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollResult;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class PollDao {

    private final JdbcTemplate jdbcTemplate;

    public PollDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertPolls(List<Poll> polls) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO poll (team_id, host_id, title, allowed_poll_count, is_anonymous, status, created_at, updated_at, closed_at, code) VALUES (?, ?, ?, ?, ?, ?, now(), now(), ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, polls.get(i).getTeam().getId());
                        ps.setLong(2, polls.get(i).getHost().getId());
                        ps.setString(3, polls.get(i).getTitle());
                        ps.setInt(4, polls.get(i).getAllowedPollCount());
                        ps.setBoolean(5, polls.get(i).getIsAnonymous());
                        ps.setString(6, polls.get(i).getStatus().name());
                        ps.setObject(7, polls.get(i).getClosedAt());
                        ps.setString(8, polls.get(i).getCode());
                    }

                    @Override
                    public int getBatchSize() {
                        return polls.size();
                    }
                }
        );
    }

    public void batchInsertPollItems(List<PollItem> pollItems) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO poll_item (poll_id, subject, created_at, updated_at) VALUES (?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, pollItems.get(i).getPoll().getId());
                        ps.setString(2, pollItems.get(i).getSubject());
                    }

                    @Override
                    public int getBatchSize() {
                        return pollItems.size();
                    }
                }
        );
    }

    public void batchInsertPollResults(List<PollResult> pollResults) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO poll_result (poll_item_id, member_id, description, created_at, updated_at) VALUES (?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, pollResults.get(i).getPollItem().getId());
                        ps.setLong(2, pollResults.get(i).getMember().getId());
                        ps.setString(3, pollResults.get(i).getDescription());
                    }

                    @Override
                    public int getBatchSize() {
                        return pollResults.size();
                    }
                }
        );
    }
}
