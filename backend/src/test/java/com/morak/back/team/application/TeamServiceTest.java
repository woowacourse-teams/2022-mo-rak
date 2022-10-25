package com.morak.back.team.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamDomainLogicException;
import com.morak.back.team.exception.TeamNotFoundException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@ServiceTest
class TeamServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private TeamService teamService;

    private Member member;

    private Team team;

    @BeforeEach
    void setUp() {
        teamService = new TeamService(
                teamRepository,
                memberRepository,
                teamMemberRepository,
                teamInvitationRepository,
                eventPublisher
        );

        member = memberRepository.save(Member.builder()
                .id(null)
                .name("송상민")
                .oauthId("94000000")
                .profileUrl("http://abur-profile")
                .build());

        team = teamRepository.save(Team.builder()
                .name("모락")
                .code(Code.generate(length -> "ABCD1234"))
                .build());

        teamMemberRepository.save(new TeamMember(null, team, member));
    }

    @Test
    void 팀을_생성한다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("test-team");

        // when
        String code = teamService.createTeam(member.getId(), request);

        // then
        assertThat(code).hasSize(8);
    }

    @Test
    void 없는_멤버로_팀_생성_시_예외를_던진다() {
        // given
        TeamCreateRequest request = new TeamCreateRequest("test-team");

        Member invalidMember = Member.builder()
                .id(9999999L)
                .oauthId("oauthId")
                .build();

        // when & then
        assertThatThrownBy(() -> teamService.createTeam(invalidMember.getId(), request))
                .isInstanceOf(MemberNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    void 그룹_초대코드를_생성한다() {
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // then
        assertThat(invitationCode).hasSize(8);
    }

    @Test
    void 그룹에_속하지않은_멤버가_그룹_초대코드를_생성_시_예외를_던진다() {
        Team 새로운팀 = teamRepository.save(Team.builder()
                .name("새로운팀")
                .code(Code.generate((length) -> "12345678"))
                .build());
        // when & then
        assertThatThrownBy(() -> teamService.createInvitationCode(member.getId(), 새로운팀.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 그룹에_참가했을_때_그룹_참가여부를_요청하면_팀코드_팀이름_true를_반환한다() {
        // given
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // when
        InvitationJoinedResponse invitationJoinedResponse = teamService.isJoined(member.getId(), invitationCode);

        // then
        assertThat(invitationJoinedResponse)
                .usingRecursiveComparison()
                .isEqualTo(new InvitationJoinedResponse(team.getCode(), team.getName(), true));
    }

    @Test
    void 그룹에_참가하지_않았을_때_그룹_참가여부를_요청하면_팀코드_팀이름_false를_반환한다() {
        // given
        Member 박성우 = memberRepository.save(Member.builder()
                .oauthId("19980513")
                .name("박성우")
                .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
                .build());

        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // when
        InvitationJoinedResponse invitationJoinedResponse = teamService.isJoined(박성우.getId(), invitationCode);

        // then
        assertThat(invitationJoinedResponse)
                .usingRecursiveComparison()
                .isEqualTo(new InvitationJoinedResponse(team.getCode(), team.getName(), false));
    }

    @Test
    void 없는_그룹코드를_이용해_그룹_참가여부를_요청하면_예외를_던진다() {
        // given
        String invalidInvitationCode = "invalidInvitationCode";

        // when & then
        assertThatThrownBy(() -> teamService.isJoined(member.getId(), invalidInvitationCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR);
    }

    @Test
    void 없는_멤버가_그룹_참가여부를_요청하면_예외를_던진다() {
        // given
        String invalidInvitationCode = "invalidInvitationCode";

        // when & then
        assertThatThrownBy(() -> teamService.isJoined(member.getId(), invalidInvitationCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR);
    }
    // TODO: 2022/08/11 시간 목킹
//    @Test
//    void 그룹_가입시_초대코드가_만료된_경우_예외를_던진다() {
//        // given
//        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());
//
//        // when
//
//        // then
//        assertThatThrownBy(() -> teamService.isJoined(member.getId(), invitationCode))
//                .isInstanceOf(TeamDomainLogicException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR);
//    }


    @Test
    void 그룹에_참가한다() {
        // given
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());
        Member 박성우 = memberRepository.save(Member.builder()
                .oauthId("19980513")
                .name("박성우")
                .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
                .build());

        // when
        String teamCode = teamService.join(박성우.getId(), invitationCode);

        // then
        assertThat(teamCode).isEqualTo(team.getCode());
    }

    @Test
    void 그룹에_이미_참가한_경우_예외를_던진다() {
        // given
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId(), invitationCode))
                .isInstanceOf(TeamDomainLogicException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_ALREADY_JOINED_ERROR);
    }

    @Test
    void 없는_그룹코드를_이용해_그룹에_참가를_요청하면_예외를_던진다() {
        // given
        String invalidInvitationCode = "invalidInvitationCode";

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId(), invalidInvitationCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_INVITATION_NOT_FOUND_ERROR);
    }

    @Test
    void 없는_멤버가_그룹에_참가를_요청하면_예외를_던진다() {
        // given
        String invitationCode = teamService.createInvitationCode(member.getId(), team.getCode());

        // when & then
        assertThatThrownBy(() -> teamService.join(member.getId() + 1, invitationCode))
                .isInstanceOf(MemberNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR);
    }
    // TODO: 2022/08/11 시간 목킹
//    @Test
//    void 그룹_가입시_초대코드가_만료된_경우_예외를_던진다() {
//        // given
//        TeamInvitation expiredTeamInvitation = TeamInvitation.builder()
//                .team(team)
//                .code(code)
//                .expiredAt(ExpiredTime.withMinute(-30L))
//                .build();
//
//        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(expiredTeamInvitation));
//
//        // when & then
//        assertThatThrownBy(() -> teamService.join(member.getId(), expiredTeamInvitation.getCode()))
//                .isInstanceOf(TeamDomainLogicException.class)
//                .extracting("code")
//                .isEqualTo(CustomErrorCode.TEAM_INVITATION_EXPIRED_ERROR);
//    }

    @Test
    void 그룹_목록을_조회한다() {
        // given
        Team teamA = teamRepository.save(Team.builder()
                .name("team-A")
                .code(Code.generate(length -> "abcd1234"))
                .build());

        teamMemberRepository.save(new TeamMember(null, teamA, member));

        // when
        List<TeamResponse> teamResponses = teamService.findTeams(member.getId());

        // then
        assertThat(teamResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(new TeamResponse(team.getId(), team.getCode(), team.getName()),
                                new TeamResponse(teamA.getId(), teamA.getCode(), teamA.getName()))
                );
    }

    @Test
    void 그룹이_없는_멤버인_경우_그룹_조회_시_그룹_목록을_받지_못한다() {
        // given
        Member 박성우 = memberRepository.save(Member.builder()
                .oauthId("19980513")
                .name("박성우")
                .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
                .build());

        // when
        List<TeamResponse> teamResponses = teamService.findTeams(박성우.getId());

        // then
        assertThat(teamResponses).hasSize(0);
    }

    @Test
    void 그룹에_속한_멤버_목록을_조회한다() {
        // given
        Member 박성우 = memberRepository.save(Member.builder()
                .oauthId("19980513")
                .name("박성우")
                .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
                .build());
        Member 이찬주 = memberRepository.save(Member.builder()
                .oauthId("19960000")
                .name("이찬주")
                .profileUrl("https://avatars.githubusercontent.com/u/52564093?v=4")
                .build());

        teamMemberRepository.save(new TeamMember(null, team, 박성우));
        teamMemberRepository.save(new TeamMember(null, team, 이찬주));

        // when
        List<MemberResponse> memberResponses = teamService.findMembersInTeam(이찬주.getId(), team.getCode());

        // then
        assertThat(memberResponses)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(
                                new MemberResponse(이찬주.getId(), 이찬주.getName(), 이찬주.getProfileUrl()),
                                new MemberResponse(member.getId(), member.getName(), member.getProfileUrl()),
                                new MemberResponse(박성우.getId(), 박성우.getName(), 박성우.getProfileUrl())
                        )
                );
    }

    @Test
    void 존재하지않는_그룹에_대해_속한_멤버_목록을_조회하면_예외를_던진다() {
        // given
        String invalidTeamCode = "invalidTeamCode";

        // when & then
        assertThatThrownBy(() -> teamService.findMembersInTeam(member.getId(), invalidTeamCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 그룹의_멤버_목록_조회_시_참가한_그룹이_아니라면_예외를_던진다() {
        // given
        Member 이찬주 = memberRepository.save(Member.builder()
                .oauthId("19960000")
                .name("이찬주")
                .profileUrl("https://avatars.githubusercontent.com/u/52564093?v=4")
                .build());

        // when & then
        assertThatThrownBy(() -> teamService.findMembersInTeam(이찬주.getId(), team.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }


    @Test
    void 멤버가_그룹에서_탈퇴한다() {
        // given
        Long memberId = member.getId();
        String teamCode = team.getCode();

        // when
        teamService.exitMemberFromTeam(memberId, teamCode);

        // then
        assertThat(teamMemberRepository.existsByTeamAndMember(team, member)).isFalse();
    }

    @Test
    void 속해있지_않은_그룹에서_탈퇴할시_예외를_던진다() {
        // given
        Team teamA = teamRepository.save(Team.builder()
                .name("team-A")
                .code(Code.generate(length -> "abcd1234"))
                .build());

        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(member.getId(), teamA.getCode()))
                .isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }

    @Test
    void 없는_그룹에서_탈퇴할시_예외를_던진다() {
        // given
        String invalidTeamCode = "invalidTeamCode";

        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(member.getId(), invalidTeamCode))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 없는_멤버가_그룹을_탈퇴할시_예외를_던진다() {
        // given
        Long invalidMemberId = member.getId() + 1;
        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(invalidMemberId, team.getCode()))
                .isInstanceOf(MemberNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    void 디폴트_그룹을_찾는다() {
        // given
        Team teamA = teamRepository.save(Team.builder()
                .name("team-A")
                .code(Code.generate(length -> "teamAAAA"))
                .build());

        Team teamB = teamRepository.save(Team.builder()
                .name("team-B")
                .code(Code.generate(length -> "teamBBBB"))
                .build());

        teamMemberRepository.save(new TeamMember(null, teamA, member));
        teamMemberRepository.save(new TeamMember(null, teamB, member));

        // when
        TeamResponse defaultTeamResponse = teamService.findDefaultTeam(member.getId());

        // then
        assertThat(defaultTeamResponse)
                .usingRecursiveComparison()
                .isEqualTo(new TeamResponse(team.getId(), team.getCode(), team.getName()));
    }

    @Test
    void 그룹에_속해있지_않은_경우_예외를_던진다() {
        // given
        Member 박성우 = memberRepository.save(Member.builder()
                .oauthId("19980513")
                .name("박성우")
                .profileUrl("https://avatars.githubusercontent.com/u/79205414?v=4")
                .build());

        // when & then
        assertThatThrownBy(() -> teamService.findDefaultTeam(박성우.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }
}
