package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@RepositoryTest
class RoleEntityRepositoryTest {

    @Qualifier("roleEntityRepositoryImpl")
    @Autowired
    private RoleEntityRepository roleEntityRepository;

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
}
