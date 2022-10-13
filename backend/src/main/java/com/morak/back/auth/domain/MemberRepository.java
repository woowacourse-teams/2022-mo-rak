package com.morak.back.auth.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findByOauthId(String oauthId);

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    List<Member> findByIdIn(Iterable<Long> ids);
}
