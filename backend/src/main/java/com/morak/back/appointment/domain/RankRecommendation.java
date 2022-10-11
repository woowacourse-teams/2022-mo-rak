package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankRecommendation {

    private final int rank;
    private final AppointmentTime appointmentTime;
    private final Set<Member> availableMembers;
    private final Set<Member> unavailableMembers;

    public static RankRecommendation from(int rank, RecommendationCell recommendationCell) {
        return new RankRecommendation(
                rank,
                recommendationCell.getAppointmentTime(),
                recommendationCell.getAvailableMembers(),
                recommendationCell.getUnavailableMembers()
        );
    }

    public LocalDateTime getStartDateTime() {
        return appointmentTime.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return appointmentTime.getEndDateTime();
    }
}
