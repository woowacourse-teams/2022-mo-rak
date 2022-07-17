package com.morak.back.auth.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    List<TeamMember> findAllByMemberId(Long memberId);

    List<TeamMember> findAllByTeamId(Long teamId);
}
