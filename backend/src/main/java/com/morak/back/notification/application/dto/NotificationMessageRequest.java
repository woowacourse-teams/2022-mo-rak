package com.morak.back.notification.application.dto;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.notification.util.MessageFormatter;
import com.morak.back.team.domain.Team;

public class NotificationMessageRequest {

    private static final String APPOINTMENT_TYPE = "appointment";
    private static final String APPOINTMENT_NAME = "약속잡기";
    private static final String POLL_TYPE = "poll";

    private final String message;

    public NotificationMessageRequest(String message) {
        this.message = message;
    }

    public static NotificationMessageRequest fromAppointmentOpen(MenuEvent event, Team team) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatOpenAnnouncement(team.getName(), APPOINTMENT_NAME, event.getTitle()),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatProgressPage(event.getTeamCode(), APPOINTMENT_TYPE, event.getCode())
        ));
    }

    public static NotificationMessageRequest fromAppointmentClosed(MenuEvent event, Team team) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatClosedAnnouncement(team.getName(), APPOINTMENT_NAME, event.getTitle()),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatResultPage(event.getTeamCode(), APPOINTMENT_TYPE, event.getCode())
        ));
    }

    public String getMessage() {
        return message;
    }
}
