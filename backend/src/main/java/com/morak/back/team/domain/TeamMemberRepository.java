package com.morak.back.team.domain;

import com.morak.back.auth.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends Repository<TeamMember, Long> {

    boolean existsByTeamAndMember(Team team, Member member);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.member = :member")
    List<TeamMember> findAllByMember(@Param("member") Member member);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.team = :team")
    List<TeamMember> findAllByTeam(@Param("team") Team team);

    Optional<TeamMember> findFirstByMemberOrderByIdAsc(@Param("member") Member member);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.team = :team AND tm.member = :member")
    Optional<TeamMember> findByTeamAndMember(@Param("team") Team team, @Param("member") Member member);

    TeamMember save(TeamMember teamMember);

    void delete(TeamMember teamMember);
}
