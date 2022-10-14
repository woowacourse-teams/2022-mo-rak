package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    // -- A

    // -- B

    @Test
    void 역할_이름을_수정한다() {
        // given
        Role role = new Role("TestTeam");
        role.updateNames(List.of("서기", "타임키퍼"));

        // when
        Role savedRole = roleRepository.save(role);

        // then
        List<RoleName> values = savedRole.getRoleNames().getValues();
        Assertions.assertAll(
                () -> assertThat(values).hasSize(2),
                () -> assertThat(values).containsExactly(new RoleName("서기"), new RoleName("타임키퍼"))
        );
    }

    // -- C

    // -- D


}
