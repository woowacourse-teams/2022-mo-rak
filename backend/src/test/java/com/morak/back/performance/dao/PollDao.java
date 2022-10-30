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
public class PollDao {

    private final JdbcTemplate jdbcTemplate;

    public PollDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertPolls(List<Entry<String, Long>> polls) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO poll (team_code, host_id, title, allowed_count, anonymous, status, created_at, updated_at, closed_at, code) VALUES (?, ?, ?, ?, ?, ?, now(), now(), ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, polls.get(i).getKey());
                        ps.setLong(2, polls.get(i).getValue());
                        ps.setString(3, "더미 투표 제목");
                        ps.setInt(4, 3);
                        ps.setBoolean(5, false);
                        ps.setString(6, "OPEN");
                        ps.setObject(7, LocalDateTime.now().plusDays(1L));
                        ps.setString(8, String.format("%08d", i + 1));
                    }

                    @Override
                    public int getBatchSize() {
                        return polls.size();
                    }
                }
        );
    }

    public void batchInsertPollItems(List<Long> pollIds) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO poll_item (poll_id, subject) VALUES (?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, pollIds.get(i));
                        ps.setString(2, "더미 투표 선택 항목");
                    }

                    @Override
                    public int getBatchSize() {
                        return pollIds.size();
                    }
                }
        );
    }

    public void batchInsertPollResults(List<Entry<Long, Long>> pollResults) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO select_member (poll_item_id, member_id, description) VALUES (?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, pollResults.get(i).getKey());
                        ps.setLong(2, pollResults.get(i).getValue());
                        ps.setString(3, "선택 이유");
                    }

                    @Override
                    public int getBatchSize() {
                        return pollResults.size();
                    }
                }
        );
    }
}
