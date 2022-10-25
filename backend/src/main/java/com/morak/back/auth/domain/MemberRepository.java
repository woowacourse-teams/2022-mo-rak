package com.morak.back.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends Repository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.oauthId.value = :oauthId")
    Optional<Member> findByOauthId(@Param("oauthId") String oauthId);

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    void flush();
}
