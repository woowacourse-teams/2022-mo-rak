package com.morak.back.core.util;

import com.morak.back.core.domain.Menu;
import com.morak.back.team.domain.Team;
import java.time.format.DateTimeFormatter;

public class MessageFormatter {

    private MessageFormatter() {
    }

    public static String formatClosed(Menu menu, Team team) {
        return String.join("\n",
                formatClosedAnnouncement(menu),
                formatTime(menu),
                formatResultPage(menu, team)
        );
    }

    private static String formatClosedAnnouncement(Menu menu) {
        return String.format("%s 팀의 %s %s 이(가) 마감되었습니다 🎉",
                menu.getTeamName(), menu.getTitle(), menu.getName()
        );
    }

    private static String formatTime(Menu menu) {
        return "마감시간 : " + menu.getClosedAt()
                .format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 H시mm분ss초"));
    }

    private static String formatResultPage(Menu menu, Team team) {
        return String.format(
                "결과 확인하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/result",
                team.getCode(), menu.getType(), menu.getCode()
        );
    }

    public static String formatOpen(Menu menu, Team team) {
        return String.join("\n",
                formatOpenAnnouncement(menu),
                formatTime(menu),
                formatProgressPage(menu, team)
        );
    }

    private static String formatOpenAnnouncement(Menu menu) {
        return String.format("%s 팀의 %s %s 이(가) 생성되었습니다 🎉",
                menu.getTeamName(), menu.getTitle(), menu.getName()
        );
    }

    private static String formatProgressPage(Menu menu, Team team) {
        return String.format(
                "진행하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/progress",
                team.getCode(), menu.getType(), menu.getCode()
        );
    }
}
