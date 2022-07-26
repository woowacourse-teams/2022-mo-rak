package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void 팀을_생성한다() {
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