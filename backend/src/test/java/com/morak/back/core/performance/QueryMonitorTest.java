package com.morak.back.core.performance;

import com.morak.back.appointment.application.AppointmentService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class QueryMonitorTest {

    @Autowired
    private QueryMonitor queryMonitor;

    @Autowired
    private AppointmentService appointmentService;

    @Test
    void 약속잡기_목록_조회_쿼리_성능을_측정한다() {
        queryMonitor.start();

        appointmentService.findAppointments("MoraK123", 1L);

        queryMonitor.end();

        System.out.printf("[쿼리 성능 측정 결과] 쿼리 개수: %d 개, 쿼리 시간: %d ms\n", queryMonitor.getCount(), queryMonitor.getTime());
    }
}
