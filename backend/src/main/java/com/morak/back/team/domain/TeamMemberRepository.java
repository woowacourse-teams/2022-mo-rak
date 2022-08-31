package com.morak.back.team.domain;

import com.morak.back.auth.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TeamMemberRepository extends Repository<TeamMember, Long> {

    boolean existsByTeamAndMember(Team team, Member member);

    List<TeamMember> findAllByMemberId(Long memberId);

    List<TeamMember> findAllByTeamId(Long teamId);

    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);

    TeamMember save(TeamMember teamMember);

    void delete(TeamMember teamMember);
}
