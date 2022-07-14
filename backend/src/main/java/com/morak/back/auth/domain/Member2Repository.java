package com.morak.back.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Member2Repository extends JpaRepository<Member2, Long> {

    Optional<Member2> findByOauthId(Long oauthId);
}
