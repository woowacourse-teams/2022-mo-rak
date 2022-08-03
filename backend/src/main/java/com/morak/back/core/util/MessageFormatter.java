package com.morak.back.core.util;

import com.morak.back.core.domain.Menu;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageFormatter {

    public static String format(Menu menu) {
        return menu.getTeamName() + "팀의 " + menu.getTitle() + " 투표가 마감되었습니다.\n" +
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
