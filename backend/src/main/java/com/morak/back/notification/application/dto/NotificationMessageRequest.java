package com.morak.back.notification.application.dto;

import com.morak.back.core.domain.MenuEvent;
import com.morak.back.notification.util.MessageFormatter;
import com.morak.back.team.domain.Team;

public class NotificationMessageRequest {

    private static final String APPOINTMENT_TYPE = "appointment";
    private static final String APPOINTMENT_NAME = "약속잡기";
    private static final String POLL_TYPE = "poll";
    private static final String POLL_NAME = "투표";

    private final String message;

    public NotificationMessageRequest(String message) {
        this.message = message;
    }

    public static NotificationMessageRequest fromAppointmentOpen(MenuEvent event, Team team) {
        return formatOpen(event, team, APPOINTMENT_TYPE, APPOINTMENT_NAME);
    }

    public static NotificationMessageRequest fromAppointmentClosed(MenuEvent event, Team team) {
        return formatClosed(event, team, APPOINTMENT_TYPE, APPOINTMENT_NAME);
    }

    public static NotificationMessageRequest fromPollOpen(MenuEvent event, Team team) {
        return formatOpen(event, team, POLL_TYPE, POLL_NAME);
    }

    public static NotificationMessageRequest fromPollClosed(MenuEvent event, Team team) {
        return formatClosed(event, team, POLL_TYPE, POLL_NAME);
    }

    public static NotificationMessageRequest formatOpen(MenuEvent event, Team team, String type, String name) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatOpenAnnouncement(team.getName(), event.getTitle(), name),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatProgressPage(event.getTeamCode(), type, event.getCode())
        ));
    }

    public static NotificationMessageRequest formatClosed(MenuEvent event, Team team, String type, String name) {
        return new NotificationMessageRequest(String.join("\n",
                MessageFormatter.formatClosedAnnouncement(team.getName(), event.getTitle(), name),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatResultPage(event.getTeamCode(), type, event.getCode())
        ));
    }

    public String getMessage() {
        return message;
    }
}
