package com.morak.back.notification.application.dto;

import com.morak.back.core.domain.menu.Menu;
import com.morak.back.notification.util.MessageFormatter;
import com.morak.back.team.domain.Team;

public class NotificationMessageRequest {

    private static final String APPOINTMENT_TYPE = "appointment";
    private static final String POLL_TYPE = "poll";

    private final String message;

    public NotificationMessageRequest(String message) {
        this.message = message;
    }

    public static NotificationMessageRequest fromAppointmentOpen(Menu menu, Team team) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatOpenAnnouncement(team.getName(), APPOINTMENT_TYPE, menu.getCode()),
                MessageFormatter.formatTime(menu.getClosedAt()),
                MessageFormatter.formatProgressPage(menu.getTeamCode(), APPOINTMENT_TYPE, menu.getCode())
        ));
    }

    public static NotificationMessageRequest fromAppointmentClosed(Menu menu, Team team) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatClosedAnnouncement(team.getName(), APPOINTMENT_TYPE, menu.getCode()),
                MessageFormatter.formatTime(menu.getClosedAt()),
                MessageFormatter.formatResultPage(menu.getTeamCode(), APPOINTMENT_TYPE, menu.getCode())
        ));
    }

    public String getMessage() {
        return message;
    }
}
