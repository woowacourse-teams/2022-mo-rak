package com.morak.back.team.domain;

import com.morak.back.auth.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends Repository<TeamMember, Long> {

    boolean existsByTeamAndMember(Team team, Member member);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.member.id = :memberId")
    List<TeamMember> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.team.id = :teamId")
    List<TeamMember> findAllByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.member.id = :memberId")
    Optional<TeamMember> findByTeamIdAndMemberId(@Param("teamId") Long teamId, @Param("memberId") Long memberId);

    TeamMember save(TeamMember teamMember);

    void delete(TeamMember teamMember);
}
