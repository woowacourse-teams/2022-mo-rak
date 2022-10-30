package com.morak.back.performance.dao;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.appointment.domain.AvailableTime;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("performance")
public class AppointmentDao {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertAppointment(List<Appointment> appointments) {
//        jdbcTemplate.batchUpdate(
//                "INSERT INTO appointment (team_code, host_id, title, description, start_date, end_date, start_time, end_time, duration_minutes, status, code, closed_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now());",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setString(1, appointments.get(i).getTeamCode());
//                        ps.setLong(2, appointments.get(i).getHostId());
//                        ps.setString(3, appointments.get(i).getTitle());
//                        ps.setString(4, appointments.get(i).getDescription());
//                        ps.setObject(5, appointments.get(i).getStartDate());
//                        ps.setObject(6, appointments.get(i).getEndDate());
//                        ps.setObject(7, appointments.get(i).getStartTime());
//                        ps.setObject(8, appointments.get(i).getEndTime());
//                        ps.setInt(9, appointments.get(i).getDurationMinutes());
//                        ps.setString(10, appointments.get(i).getStatus().name());
//                        ps.setString(11, appointments.get(i).getCode());
//                        ps.setObject(12, appointments.get(i).getClosedAt());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return appointments.size();
//                    }
//                }
//        );
    }

    public void batchInsertAvailableTime(List<AvailableTime> availableTimes) {
//        jdbcTemplate.batchUpdate(
//                "INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, end_date_time, created_at, updated_at) VALUES (?, ?, ?, ?, now(), now());",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setLong(1, availableTimes.get(i).getAppointment().getId());
//                        ps.setLong(2, availableTimes.get(i).getMember().getId());
//                        ps.setObject(3, availableTimes.get(i).getDateTimePeriod().getStartDateTime());
//                        ps.setObject(4, availableTimes.get(i).getDateTimePeriod().getEndDateTime());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return availableTimes.size();
//                    }
//                }
//        );
    }
}
