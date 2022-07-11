package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("팀을 생성한다")
    @Test
    void saveTeam() {
        // given
        Team team = new Team(null, "test-team", "test-code");

        // when
        Team savedTeam = teamRepository.save(team);

        // then
        Assertions.assertAll(
            () -> assertThat(savedTeam).isNotNull(),
            () -> assertThat(savedTeam.getId()).isNotNull(),
            () -> assertThat(savedTeam.getName()).isEqualTo("test-team")
        );

    }
}