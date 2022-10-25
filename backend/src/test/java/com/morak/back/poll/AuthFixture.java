package com.morak.back.poll;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
import com.morak.back.team.domain.Team;

public class AuthFixture {

    public static Team createTeam(long id, String name, String code) {
        return Team.builder()
                .id(id)
                .name(name)
                .code(Code.generate(l -> code)).build();
    }

    public static Member createMember(long id, String oAuthId, String name, String profileUrl) {
        return Member.builder()
                .id(id)
                .oauthId(oAuthId)
                .name(name)
                .profileUrl(profileUrl)
                .build();
    }
}
