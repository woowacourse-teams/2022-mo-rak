package com.morak.back.appointment.domain.appointment.timeconditions;

import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosedAt {

    private LocalDateTime closedAt;

    public static ClosedAt of(LocalDateTime closedAt, LocalDateTime endDateTime, LocalDateTime now) {
        validateNow(closedAt, now);
        validateBeforeEndDateTime(closedAt, endDateTime);
        return new ClosedAt(closedAt);
    }

    private static void validateNow(LocalDateTime closedAt, LocalDateTime now) {
        if (closedAt.isBefore(now)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR,
                    String.format("약속잡기의 마감 날짜/시각(%s)은 지금(%s)보다 이후여야합니다.", closedAt, now)
            );
        }
    }

    private static void validateBeforeEndDateTime(LocalDateTime closedAt, LocalDateTime endDateTime) {
        if (closedAt.isAfter(endDateTime)) {
            throw new AppointmentDomainLogicException(
                    CustomErrorCode.APPOINTMENT_CLOSED_AT_OUT_OF_RANGE_ERROR,
                    String.format("약속잡기의 마감 날짜/시각(%s)은 마지막 날짜/시각(%s)보다 이후여야합니다.", closedAt, endDateTime)
            );
        }
    }
}
