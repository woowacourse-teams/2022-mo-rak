package com.morak.back.role.domain;

import java.util.Optional;

public interface RoleEntityRepository {

    /**
     * roleNames 는 null 을 반환함에 주의한다.
     * @param teamCode
     * @return 날짜별 최신 히스토리를 가진 Role
     */
    Optional<Role> findWithOnlyHistoriesPerDate(String teamCode);
}
