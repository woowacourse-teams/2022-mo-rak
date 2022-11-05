package com.morak.back.role.domain;

import java.util.Optional;

public interface RoleEntityRepository {

    /**
     * 날짜별 최신 히스토리를 가진 역할을 반환한다.
     * roleNames 는 null 을 반환함에 주의한다.
     * @param teamCode
     * @return
     */
    Optional<Role> findWithOnlyHistoriesPerDate(String teamCode);
}
