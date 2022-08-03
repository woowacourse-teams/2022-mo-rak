package com.morak.back.core.domain;

import com.morak.back.auth.domain.Member;

public interface Menu {

    String getTeamName();

    String getTitle();

    void close(Member host);

    Member getHost();
}
