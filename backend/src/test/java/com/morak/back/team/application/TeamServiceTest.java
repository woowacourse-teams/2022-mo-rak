package com.morak.back.team.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

    @InjectMocks
    private TeamService teamService;

    @DisplayName("팀을 생성한다.")
    @Test
    void createTeam() {
        // given
        given(teamRepository.save(any())).willReturn(new Team(1L, "test-team", "test-code"));
        TeamCreateRequest request = new TeamCreateRequest("test-team");

        // when
        Long savedId = teamService.createTeam(request);

        // then
        assertThat(savedId).isEqualTo(1L);
    }
}