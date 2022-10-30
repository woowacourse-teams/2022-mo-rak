package com.morak.back.notification.domain.slack;

import com.morak.back.appointment.domain.Appointment;
import com.morak.back.core.support.Generated;
import com.morak.back.poll.domain.Poll;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Generated
public class FormattableData {

    private final String type;
    private final String name;
    private final String teamName;
    private final String title;
    private final String code;
    private final String teamCode;
    private final LocalDateTime closedAt;

    public static FormattableData from(Appointment appointment) {
        return new FormattableData(
                "appointment",
                "약속잡기",
                appointment.getTeamCode(),
                appointment.getTitle(),
                appointment.getCode(),
                appointment.getTeamCode(),
                appointment.getClosedAt()
        );
    }

    public static FormattableData from(Poll poll) {
        return new FormattableData(
                "poll",
                "투표",
                String.valueOf(poll.getTeamCode()),
                poll.getTitle(),
                poll.getCode(),
                String.valueOf(poll.getTeamCode()),
                poll.getClosedAt()
        );
    }
}
