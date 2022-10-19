package com.morak.back.poll;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.team.domain.Team;

public class AuthFixture {

    public static final Team 모락 = Team.builder()
            .name("모락")
            .code(Code.generate(l -> "mrmrcode"))
            .build();

    public static final Member 엘리 = Member.builder()
            .oauthId("ellie-oauthid")
            .name("엘리")
            .profileUrl("https://ellie-profile")
            .build();

    public static final Member 에덴 = Member.builder()
            .oauthId("cool-guy")
            .name("에덴")
            .profileUrl("https://eden-profile")
            .build();

    public static Team createTeam(long id, String name, String code) {
        return Team.builder()
                .id(id)
                .name(name)
                .code(Code.generate(l -> code)).build();
    }

    public static Member createMember(long id, String name) {
        return Member.builder()
                .id(id)
                .name(name)
                .build();
    }
}
