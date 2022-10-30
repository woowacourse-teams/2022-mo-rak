package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.core.domain.Code;
import com.morak.back.support.RepositoryTest;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TeamMemberRepositoryTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Member member;
    private Team team;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(
                Member.builder()
                        .oauthId("member-oauth-id")
                        .name("member-name")
                        .profileUrl("http://member-profile-url")
                        .build()
        );
        team = teamRepository.save(
                Team.builder()
                        .code(Code.generate(ignored -> "teamcode"))
                        .name("team-name")
                        .build()
        );
    }

    @Test
    void 팀멤버를_저장한다() {
        // given
        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .build();
        // when
        TeamMember saveMember = teamMemberRepository.save(teamMember);
        // then
        assertThat(saveMember.getId()).isNotNull();
    }

    @Test
    void 팀멤버를_삭제한다() {
        // given
        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .build();
        teamMemberRepository.save(teamMember);
        teamMemberRepository.delete(teamMember);

        // when
        Optional<TeamMember> deletedTeamMember = teamMemberRepository.findByTeamAndMember(team, member);

        // then
        assertThat(deletedTeamMember).isEmpty();
    }

    @Test
    void 팀과_멤버로_팀멤버가_존재하는지_확인한다() {
        // given
        teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
        // when
        boolean exists = teamMemberRepository.existsByTeamAndMember(team, member);
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 멤버로_모든_팀을_찾는다() {
        // given
        TeamMember teamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
        // when
        List<TeamMember> teamMembers = teamMemberRepository.findAllByMember(member);
        // then
        assertThat(teamMembers).hasSize(1)
                .containsExactly(teamMember);
    }

    @Test
    void 팀으로_모든_멤버를_찾는다() {
        // given
        TeamMember teamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
        // when
        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeam(team);
        // then
        assertThat(teamMembers).hasSize(1)
                .containsExactly(teamMember);
    }

    @Test
    void 멤버로_가장_먼저_생성된_팀멤버를_찾는다() {
        // given
        TeamMember teamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
        Team otherTeam = teamRepository.save(
                Team.builder()
                        .code(Code.generate(ignored -> "codeteam"))
                        .name("other-team-name")
                        .build()
        );
        TeamMember otherTeamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(otherTeam)
                .build());
        // when
        TeamMember foundTeamMember = teamMemberRepository.findFirstByMemberOrderByIdAsc(member).orElseThrow();
        // then
        assertThat(foundTeamMember).isEqualTo(teamMember)
                .isNotEqualTo(otherTeamMember);
    }

    @Test
    void 팀과_멤버로_팀멤버를_조회한다() {
        // given
        TeamMember teamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
        // when
        TeamMember findTeamMember = teamMemberRepository.findByTeamAndMember(team, member).orElseThrow();
        // then
        assertThat(findTeamMember).isEqualTo(teamMember);
    }

    @Test
    void team_code와_member_id로_team_member를_삭제한다(@Autowired EntityManager entityManager) {
        // given
        TeamMember teamMember = teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());

        teamMemberRepository.deleteByTeamCodeAndMemberId(team.getCode(), member.getId());
        entityManager.flush();

        // when
        Optional<TeamMember> teamAndMember = teamMemberRepository.findByTeamAndMember(team, member);

        // then
        assertThat(teamAndMember).isEmpty();
    }
}
