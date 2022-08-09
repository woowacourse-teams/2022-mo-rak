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
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.ResourceNotFoundException;
import com.morak.back.team.domain.ExpiredTime;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamDomainLogicException;
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
    private Code code;

    @BeforeEach
    void setup() {
        member = Member.builder()
                .id(1L)
                .oauthId("12345678")
                .name("ellie")
                .profileUrl("http://ellie-profile")
                .build();
        team = Team.builder()
                .id(1L)
                .name("team")
                .code(Code.generate(length -> "ABCD1234"))
                .build();
        teamInvitation = TeamInvitation.builder()
                .id(1L)
                .team(team)
                .code(Code.generate(length -> "12345678"))
                .expiredAt(ExpiredTime.withMinute(30L))
                .build();
        code = Code.generate(length -> "abcd1234");
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
                .isInstanceOf(TeamDomainLogicException.class);
    }

    @Test
    void 그룹_가입시_초대코드가_만료된_경우_예외를_던진다() {
        // given
        TeamInvitation expiredTeamInvitation = TeamInvitation.builder()
                .team(team)
                .code(code)
                .expiredAt(ExpiredTime.withMinute(-30L))
                .build();

        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(expiredTeamInvitation));

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId(), expiredTeamInvitation.getCode()))
                .isInstanceOf(TeamDomainLogicException.class);
    }

    @Test
    void 그룹_목록을_조회한다() {
        // given
        Code code = Code.generate(length -> "abcd1234");
        Team teamA = Team.builder()
                .name("team-A")
                .code(code)
                .build();
        Team teamB = Team.builder()
                .name("team-B")
                .code(code)
                .build();

        given(teamMemberRepository.findAllByMemberId(anyLong())).willReturn(
                List.of(
                        TeamMember.builder().id(1L)
                                .team(teamA)
                                .member(member)
                                .build(),
                        TeamMember.builder().id(2L)
                                .team(teamB)
                                .member(member)
                                .build()
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
        Member member1 = Member.builder()
                .id(1L)
                .oauthId("oauthId1")
                .name("name1")
                .profileUrl("http://123-profile.com")
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .oauthId("oauthId2")
                .name("name2")
                .profileUrl("http://234-profile.com")
                .build();
        TeamMember teamMember1 = TeamMember.builder().id(1L)
                .team(team)
                .member(member1)
                .build();
        TeamMember teamMember2 = TeamMember.builder().id(2L)
                .team(team)
                .member(member2)
                .build();

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
                .isInstanceOf(TeamAuthorizationException.class);
    }

    @Test
    void 멤버가_그룹에서_탈퇴한다() {
        // given
        TeamMember teamMember = TeamMember.builder().id(1L)
                .team(team)
                .member(member)
                .build();

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
                .willThrow(TeamAuthorizationException.class);

        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(member.getId(), team.getCode()))
                .isInstanceOf(TeamAuthorizationException.class);
    }

    @Test
    void 디폴트_그룹을_찾는다() {
        // given
        Team teamA = Team.builder()
                .id(1L)
                .name("team-A")
                .code(code)
                .build();
        Team teamB = Team.builder()
                .id(2L)
                .name("team-B")
                .code(code)
                .build();
        TeamMember teamMember1 = TeamMember.builder().id(1L)
                .team(teamA)
                .member(member)
                .build();
        TeamMember teamMember2 = TeamMember.builder().id(2L)
                .team(teamB)
                .member(member)
                .build();

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
