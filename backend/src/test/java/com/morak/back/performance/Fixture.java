package com.morak.back.performance;

import com.morak.back.auth.domain.Member;
import com.morak.back.team.domain.Team;

public class Fixture {

    public static final Member MEMBER_ID1 = Member.builder().id(1L).build();
    public static final Member MEMBER_ID2 = Member.builder().id(2L).build();

    public static final Team TEAM_ID1 = Team.builder().id(1L).build();
}
