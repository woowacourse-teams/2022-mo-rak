package com.morak.back.team.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class InvitationJoinedResponse {

    private String groupCode;
    private String name;
    private Boolean isJoined;

    @JsonCreator
    public InvitationJoinedResponse(String groupCode, String name, boolean isJoined) {
        this.groupCode = groupCode;
        this.name = name;
        this.isJoined = isJoined;
    }
}
