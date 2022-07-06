package com.morak.back.auth.ui.dto;

import com.morak.back.auth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberResponse {

    private Long id;
    private String name;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName()
        );
    }
}
