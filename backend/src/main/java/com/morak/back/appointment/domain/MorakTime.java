package com.morak.back.appointment.domain;

import java.time.LocalDateTime;

public interface MorakTime {

    void changeTime(LocalDateTime dateTime);

    LocalDateTime now();
}
