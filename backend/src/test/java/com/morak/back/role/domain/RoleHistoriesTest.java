package com.morak.back.role.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoleHistoriesTest {

    @Test
    void 날짜별로_최신의_히스토리만_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 10, 14, 22, 52);
        RoleName 데일리_마스터 = new RoleName("데일리 마스터");
        RoleName 서기 = new RoleName("서기");
        RoleHistories roleHistories = new RoleHistories();

        RoleHistory history1 = new RoleHistory(now, List.of(new RoleMatchResult(데일리_마스터, 1L)));
        RoleHistory history2 = new RoleHistory(now.plusSeconds(10), List.of(new RoleMatchResult(서기, 2L)));
        RoleHistory history3 = new RoleHistory(now.minusDays(1), List.of(new RoleMatchResult(데일리_마스터, 2L)));
        RoleHistory history4 = new RoleHistory(now.minusDays(1).plusSeconds(10), List.of(new RoleMatchResult(서기, 1L)));

        roleHistories.add(history1);
        roleHistories.add(history2);
        roleHistories.add(history3);
        roleHistories.add(history4);

        // when
        List<RoleHistory> histories = roleHistories.findAllGroupByDate();

        // then
        Assertions.assertAll(
                () -> assertThat(histories).hasSize(2),
                () -> assertThat(histories).containsExactly(history2, history4)
        );
    }
}
