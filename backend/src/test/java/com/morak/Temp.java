package com.morak;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class Temp {

    @Test
    void test() {
        Duration A = Duration.ofMinutes(10);
        Duration B = Duration.ofMinutes(100);
        System.out.println("A.compareTo(B) = " + A.compareTo(B));
    }
}
