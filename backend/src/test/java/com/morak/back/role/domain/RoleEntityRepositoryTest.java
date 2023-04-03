package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RepositoryTest
class RoleEntityRepositoryTest {

    private RoleRepository roleRepository;
    @Qualifier("roleEntityRepositoryImpl")
    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    public RoleEntityRepositoryTest(RoleRepository roleRepository, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.roleEntityRepository = new RoleEntityRepositoryImpl(entityManager);
    }

    @Test
    void 날짜별_최신_히스토리를_가진_역할을_조회한다() {
        // given
        Role role = roleEntityRepository.findWithOnlyHistoriesPerDate("roletest").orElseThrow();
        // when & then
        Assertions.assertAll(
                () -> assertThat(role.getRoleNames()).isNull(),
                () -> assertThat(role.getRoleHistories().getValues()).hasSize(3)
        );
    }

    @Test
    void 히스토리가_없어도_역할은_조회할_수_있다() {
        // given
        String teamCode = "testTeam";
        roleRepository.save(new Role(teamCode));

        // when
        Optional<Role> role = roleEntityRepository.findWithOnlyHistoriesPerDate(teamCode);

        // then
        assertThat(role).isPresent();
    }

    @Test
    void 없는_역할을_조회한다() {
        // given
        String teamCode = "invalidT";

        // when
        Optional<Role> role = roleEntityRepository.findWithOnlyHistoriesPerDate(teamCode);

        // then
        assertThat(role).isEmpty();
    }
}
