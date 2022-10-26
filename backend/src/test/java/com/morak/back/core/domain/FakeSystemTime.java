package com.morak.back.core.domain;

import com.morak.back.appointment.domain.SystemTime;
import com.morak.back.support.FakeBean;
import java.time.LocalDateTime;

@FakeBean
public class FakeSystemTime extends SystemTime {
    public FakeSystemTime() {
        super(LocalDateTime.now());
    }
}
