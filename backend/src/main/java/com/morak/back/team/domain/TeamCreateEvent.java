package com.morak.back.team.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamCreateEvent {
    private final String teamCode;
}
