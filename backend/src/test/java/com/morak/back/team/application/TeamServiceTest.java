package com.morak.back.team.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.TeamMemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.team.ui.dto.TeamCreateRequest;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private TeamService teamService;

    @DisplayName("팀을 생성한다.")
    @Test
    void createTeam() {
        // given
        Long memberId = 1L;
        given(teamRepository.save(any())).willReturn(new Team(1L, "test-team", "abCD1234"));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(new Member(
                        1L,
                        "test-oauth-id",
                        "test-name",
                        "https://image-url.com"
                )));
        TeamCreateRequest request = new TeamCreateRequest("test-team");


        // when
        String code = teamService.createTeam(memberId, request);

        // then
        assertThat(code).isEqualTo("abCD1234");
    }
}