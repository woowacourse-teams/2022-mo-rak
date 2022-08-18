package com.morak.back.core.util;

import com.morak.back.core.domain.slack.FormattableData;
import com.morak.back.core.support.Generated;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Generated
public class MessageFormatter {

    public static String formatClosed(FormattableData data) {
        return String.join("\n",
                formatClosedAnnouncement(data),
                formatTime(data),
                formatResultPage(data)
        );
    }

    private static String formatClosedAnnouncement(FormattableData data) {
        return String.format("%s 팀의 %s %s 이(가) 마감되었습니다 🎉",
                data.getTeamName(), data.getTitle(), data.getName()
        );
    }

    private static String formatTime(FormattableData data) {
        return "마감시간 : " + data.getClosedAt()
                .format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 H시mm분ss초"));
    }

    private static String formatResultPage(FormattableData data) {
        return String.format(
                "결과 확인하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/result",
                data.getTeamCode(), data.getType(), data.getCode()
        );
    }

    public static String formatOpen(FormattableData data) {
        return String.join("\n",
                formatOpenAnnouncement(data),
                formatTime(data),
                formatProgressPage(data)
        );
    }

    private static String formatOpenAnnouncement(FormattableData data) {
        return String.format("%s 팀의 %s %s 이(가) 생성되었습니다 🎉",
                data.getTeamName(), data.getTitle(), data.getName()
        );
    }

    private static String formatProgressPage(FormattableData data) {
        return String.format(
                "진행하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/progress",
                data.getTeamCode(), data.getType(), data.getCode()
        );
    }
}
