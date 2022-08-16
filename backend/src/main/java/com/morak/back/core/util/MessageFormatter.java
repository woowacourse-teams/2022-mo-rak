package com.morak.back.core.util;

import com.morak.back.core.domain.Menu;
import com.morak.back.team.domain.Team;

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
        return "마감시간 : " + menu.getClosedAt();
    }

    private static String formatResultPage(Menu menu, Team team) {
        return String.format(
                "투표결과 확인하러 가기 ! -> https://mo-rak.com/groups/%s/poll/%s/result",
                team.getCode(), menu.getCode()
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
                "투표하러 가기 ! -> https://mo-rak.com/groups/%s/poll/%s/progress",
                team.getCode(), menu.getCode()
        );
    }
}
