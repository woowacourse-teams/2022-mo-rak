package com.morak.back.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);
}
