package com.morak.back.performance.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("performance")
public class AppointmentDao {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void batchInsertAppointment(List<DummyAppointment> appointments) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO appointment (team_code, host_id, title, sub_title, start_date, end_date, start_time, end_time, duration_minutes, status, code, closed_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, appointments.get(i).getTeamCode());
                        ps.setLong(2, appointments.get(i).getHostId());
                        ps.setString(3, appointments.get(i).getTitle());
                        ps.setString(4, appointments.get(i).getSubTitle());
                        ps.setObject(5, appointments.get(i).getStartDate());
                        ps.setObject(6, appointments.get(i).getEndDate());
                        ps.setObject(7, appointments.get(i).getStartTime());
                        ps.setObject(8, appointments.get(i).getEndTime());
                        ps.setInt(9, appointments.get(i).getDurationMinutes());
                        ps.setString(10, appointments.get(i).getStatus().name());
                        ps.setString(11, appointments.get(i).getCode());
                        ps.setObject(12, appointments.get(i).getClosedAt());
                    }

                    @Override
                    public int getBatchSize() {
                        return appointments.size();
                    }
                }
        );
    }

    public void batchInsertAvailableTime(List<DummyAvailableTime> availableTimes) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, created_at) VALUES (?, ?, ?, now());",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, availableTimes.get(i).getAppointmentId());
                        ps.setLong(2, availableTimes.get(i).getMemberId());
                        ps.setObject(3, availableTimes.get(i).getStartDateTime());
                    }

                    @Override
                    public int getBatchSize() {
                        return availableTimes.size();
                    }
                }
        );
    }
}
