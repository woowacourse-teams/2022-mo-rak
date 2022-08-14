package com.morak.back.team.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationJoinedResponse {

    private String groupCode;
    private String name;
    private Boolean isJoined;
}
