package com.morak.back.core.domain;

import com.morak.back.support.FakeBean;
import java.time.LocalDateTime;

@FakeBean
public class FakeSystemTime extends SystemTime {
    public FakeSystemTime() {
        super(LocalDateTime.of(2022, 10, 19, 14, 17, 0));
    }
}
