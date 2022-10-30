package com.morak.back.notification.util;

import com.morak.back.core.support.Generated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Generated
public class MessageFormatter {

    public static String formatClosedAnnouncement(String teamName, String title, String menuName) {
        return String.format("%s 팀의 '%s' %s 이(가) 마감되었습니다 🎉", teamName, title, menuName);
    }

    public static String formatTime(LocalDateTime dateTime) {
        return "마감시간 : " + dateTime.format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 H시mm분ss초"));
    }

    public static String formatResultPage(String teamCode, String type, String code) {
        return String.format("결과 확인하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/result", teamCode, type, code);
    }

    public static String formatOpenAnnouncement(String teamName, String title, String menuName) {
        return String.format("%s 팀의 %s %s 이(가) 생성되었습니다 🎉", teamName, title, menuName);
    }

    public static String formatProgressPage(String teamCode, String type, String code) {
        return String.format("진행하러 가기 ! -> https://mo-rak.com/groups/%s/%s/%s/progress", teamCode, type, code);
    }
}
