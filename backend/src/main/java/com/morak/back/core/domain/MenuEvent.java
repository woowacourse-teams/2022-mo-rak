package com.morak.back.core.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class MenuEvent {

    private final String code;
    private final String teamCode;
    private final String title;
    private final LocalDateTime closedAt;
    private final boolean closed;
}
