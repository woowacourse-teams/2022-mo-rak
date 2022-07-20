package com.morak.back.poll.ui.dto;

import com.morak.back.auth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberResultResponse {

    private Long id;
    private String name;
    private String profileUrl;
    private String description;

    public static MemberResultResponse of(Member member, String description) {
        return new MemberResultResponse(
                member.getId(),
                member.getName(),
                member.getProfileUrl(),
                description
        );
    }
}
