package com.morak.back.core.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;

public interface Menu {

    String getType();

    String getName();

    String getTeamName();

    String getTitle();

    String getCode();

    void close(Member host);

    Member getHost();

    LocalDateTime getClosedAt();
}
