package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.*;

import com.morak.back.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;


    // -- A



    // -- B
    
    @Test
    void 테스트() {
        // given
        Role role = new Role("TestTeam");
        // when
        role.updateNames(List.of("서기", "타임키퍼"));
        // then
        roleRepository.save(role);
    }

    // -- C



    // -- D


}
