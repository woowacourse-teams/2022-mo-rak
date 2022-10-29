package com.morak.back.notification.application.dto;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.MenuEvent;
import com.morak.back.notification.util.MessageFormatter;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationMessageRequest {

    // todo : message formatter 역할 구분하기
    private static final String APPOINTMENT_TYPE = "appointment";
    private static final String APPOINTMENT_NAME = "약속잡기";
    private static final String POLL_TYPE = "poll";
    private static final String POLL_NAME = "투표";
    private static final String NEW_LINE = "\n";

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

    private static NotificationMessageRequest formatOpen(MenuEvent event, Team team, String type, String name) {
        return new NotificationMessageRequest(String.join(NEW_LINE,
                MessageFormatter.formatOpenAnnouncement(team.getName(), event.getTitle(), name),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatProgressPage(event.getTeamCode(), type, event.getCode())
        ));
    }

    private static NotificationMessageRequest formatClosed(MenuEvent event, Team team, String type, String name) {
        return new NotificationMessageRequest(String.join(NEW_LINE,
                MessageFormatter.formatClosedAnnouncement(team.getName(), event.getTitle(), name),
                MessageFormatter.formatTime(event.getClosedAt()),
                MessageFormatter.formatResultPage(event.getTeamCode(), type, event.getCode())
        ));
    }

    public static NotificationMessageRequest fromRoleHistory(
            LocalDateTime dateTime,
            Team team,
            Map<Member, String> roleNameByMembers
    ) {
        return new NotificationMessageRequest(String.join(NEW_LINE,
                team.getName() + "의 역할이 정해졌습니다 🥳",
                "생성 시각 : " + dateTime.format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 H시mm분ss초")),
                roleNameByMembers.entrySet().stream()
                        .map(map -> map.getKey().getName() + " : " + map.getValue())
                        .collect(Collectors.joining(NEW_LINE))
        ));
    }

    public String getMessage() {
        return message;
    }
}
