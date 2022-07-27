package com.morak.back.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends Repository<Member, Long> {

    @Query("select m from Member m where m.oauthId.oauthId = :oauthId")
    Optional<Member> findByOauthId(@Param("oauthId") String oauthId);

    Member save(Member member);

    Optional<Member> findById(Long memberId);
}
