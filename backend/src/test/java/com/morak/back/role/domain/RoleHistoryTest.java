package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class RoleHistoryTest {

    @Test
    void RoleHistory는_dateTime에_따라_크기_비교가_된다() {
        // given
        RoleHistory roleHistoryA = new RoleHistory(LocalDateTime.now(), new ArrayList<>(), 1L);
        RoleHistory roleHistoryB = new RoleHistory(LocalDateTime.now().plusDays(1), new ArrayList<>(), 1L);
        RoleHistory roleHistoryC = new RoleHistory(LocalDateTime.now().plusDays(2), new ArrayList<>(), 1L);

        // when & then
        assertAll(
                () -> assertThat(roleHistoryB).isGreaterThan(roleHistoryA),
                () -> assertThat(roleHistoryB).isLessThan(roleHistoryC)
        );
    }
}
