package com.morak.back.team.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.team.domain.ExpiredTime;
import com.morak.back.team.domain.InvitationCode;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamInvitationRepository teamInvitationRepository;


    @InjectMocks
    private TeamService teamService;

    private Member member;
    private Team team;
    private TeamInvitation teamInvitation;

    @BeforeEach
    void setup() {
        member = new Member(1L, "12345678", "ellie", "ellie-profile");
        team = new Team(1L, "team", "ABCD1234");
        teamInvitation = new TeamInvitation(1L, team, InvitationCode.generate((length) -> "inviteCode"),
                ExpiredTime.withMinute(30L));
    }

    @Test
    void 팀을_생성한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("test-team");

        given(teamRepository.save(any())).willReturn(team);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        String code = teamService.createTeam(member.getId(), request);

        // then
        assertThat(code).isEqualTo(team.getCode());
    }

    @Test
    void 그룹_초대코드를_생성한다() {
        // given
        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(teamInvitationRepository.save(any())).willReturn(teamInvitation);

        // when
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // then
        assertThat(invitationCode).isEqualTo(teamInvitation.getCode());
    }

    @Test
    void 그룹_참가_여부를_확인한다() {
        // given
        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(teamInvitation));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // when
        InvitationJoinedResponse invitationJoinedResponse = teamService.isJoined(1L, "inviteCode");

        // then
        Assertions.assertAll(
                () -> assertThat(invitationJoinedResponse.getGroupCode()).isEqualTo(team.getCode()),
                () -> assertThat(invitationJoinedResponse.getName()).isEqualTo(team.getName()),
                () -> assertThat(invitationJoinedResponse.getIsJoined()).isTrue()
        );
    }

    @Test
    void 그룹에_참가한다() {
        // given
        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(teamInvitation));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(false);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(teamMemberRepository.save(any())).willReturn(any());

        // when
        String teamCode = teamService.join(member.getId(), teamInvitation.getCode());

        // then
        assertThat(teamCode).isEqualTo(team.getCode());
    }

    @Test
    void 그룹에_이미_참가한_경우_예외를_던진다() {
        // given
        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(teamInvitation));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId(), teamInvitation.getCode()))
                .isInstanceOf(AlreadyJoinedTeamException.class);
    }

    @Test
    void 초대코드가_만료된_경우_예외를_던진다() {
        // given
        TeamInvitation expiredTeamInvitation = new TeamInvitation(null, team,
                InvitationCode.generate((length) -> "invitecode"),
                ExpiredTime.withMinute(0L));

        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(expiredTeamInvitation));

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId(), expiredTeamInvitation.getCode()))
                .isInstanceOf(ExpiredInvitationException.class);
    }

    @Test
    void 그룹_목록을_조회한다() {
        // given
        Team teamA = new Team(null, "team-A", "testcode");
        Team teamB = new Team(null, "team-B", "testcoed");

        given(teamMemberRepository.findAllByMemberId(anyLong())).willReturn(
                List.of(
                        new TeamMember(1L, teamA, member),
                        new TeamMember(2L, teamB, member)
                )
        );

        // when
        List<TeamResponse> teamResponses = teamService.findTeams(member.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(teamResponses).hasSize(2),
                () -> assertThat(teamResponses).extracting("name", "code").containsExactly(
                        tuple(teamA.getName(), teamA.getCode()),
                        tuple(teamB.getName(), teamB.getCode())
                )
        );
    }

    @Test
    void 그룹에_속한_멤버_목록을_조회한다() {
        // given
        Member member1 = new Member(1L, "oauthId1", "name1", "123");
        Member member2 = new Member(2L, "oauthId2", "name2", "234");
        TeamMember teamMember1 = new TeamMember(1L, team, member1);
        TeamMember teamMember2 = new TeamMember(2L, team, member2);

        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(teamMemberRepository.findAllByTeamId(anyLong())).willReturn(List.of(teamMember1, teamMember2));

        // when
        List<MemberResponse> memberResponses = teamService.findMembersInTeam(member1.getId(), team.getCode());

        // then
        assertThat(memberResponses).extracting("id", "name", "profileUrl")
                .containsExactly(
                        tuple(member1.getId(), member1.getName(), member1.getProfileUrl()),
                        tuple(member2.getId(), member2.getName(), member2.getProfileUrl())
                );
    }

    @Test
    void 그룹의_멤버_목록_조회_시_참가한_그룹이_아니라면_예외를_던진다() {
        // given
        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> teamService.findMembersInTeam(member.getId(), team.getCode()))
                .isInstanceOf(MismatchedTeamException.class);
    }

    @Test
    void 멤버가_그룹에서_탈퇴한다() {
        // given
        TeamMember teamMember = new TeamMember(1L, team, member);

        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.findByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(teamMember));

        // when
        teamService.exitMemberFromTeam(member.getId(), team.getCode());

        // then
        verify(teamMemberRepository).delete(any());
    }

    @Test
    void 속해있지_않은_그룹에서_탈퇴할시_예외를_던진다() {
        // given
        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.findByTeamIdAndMemberId(anyLong(), anyLong()))
                .willThrow(MismatchedTeamException.class);

        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(member.getId(), team.getCode()))
                .isInstanceOf(MismatchedTeamException.class);
    }

    @Test
    void 디폴트_그룹을_찾는다() {
        // given
        Team teamA = new Team(1L, "team-A", "testcode");
        Team teamB = new Team(2L, "team-B", "testcoed");
        TeamMember teamMember1 = new TeamMember(1L, teamA, member);
        TeamMember teamMember2 = new TeamMember(2L, teamB, member);

        given(teamMemberRepository.findAllByMemberId(anyLong())).willReturn(List.of(teamMember1, teamMember2));

        // when
        TeamResponse defaultTeamResponse = teamService.findDefaultTeam(member.getId());
        // then
        assertThat(defaultTeamResponse).extracting("code", "name")
                .containsExactly(teamA.getCode(), teamA.getName());
    }

    @Test
    void 그룹에_속해있지_않은_경우_예외를_던진다() {
        // given
        given(teamMemberRepository.findAllByMemberId(anyLong())).willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> teamService.findDefaultTeam(member.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
