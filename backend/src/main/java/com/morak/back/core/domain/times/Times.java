package com.morak.back.core.domain.times;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Times {

    LocalDateTime dateTimeOfNow();

    LocalDate dateOfNow();

    void changeTime(LocalDateTime dateTime);

    void reset();
}
