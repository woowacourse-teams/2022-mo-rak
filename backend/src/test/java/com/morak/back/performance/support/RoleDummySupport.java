package com.morak.back.performance.support;

import com.morak.back.performance.dao.RoleDao;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("performance")
public class RoleDummySupport {

    @Autowired
    private RoleDao roleDao;

    public void 역할_더미데이터를_추가한다(List<String> teamCodes) {
        roleDao.batchInsertRoles(teamCodes);
    }

    public void 역할_이름_더미데이터를_추가한다() {
        // 1번 role에 4개의 역할 이름을 추가한다.
        List<Entry<Long, String>> roleNames = IntStream.rangeClosed(1, 3)
                .mapToObj(idx -> new SimpleEntry<Long, String>(1L, "데일리 마스터"))
                .collect(Collectors.toList());
        roleDao.batchInsertRoleNames(roleNames);
    }

    public void 역할_히스토리_더미데이터를_추가한다() {
        List<LocalDateTime> dateTimes = IntStream.rangeClosed(0, 10_000)
                .mapToObj(idx -> LocalDateTime.now().minusDays(idx))
                .collect(Collectors.toList());
        roleDao.batchInsertRoleHistories(1L, dateTimes);
    }
}
