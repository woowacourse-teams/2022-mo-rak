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
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamMember;
import com.morak.back.auth.domain.TeamMemberRepository;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.auth.ui.dto.MemberResponse;
import com.morak.back.poll.exception.ResourceNotFoundException;
import com.morak.back.team.domain.ExpiredTime;
import com.morak.back.team.domain.InvitationCode;
import com.morak.back.team.domain.TeamInvitation;
import com.morak.back.team.domain.TeamInvitationRepository;
import com.morak.back.team.exception.AlreadyJoinedTeamException;
import com.morak.back.team.exception.ExpiredInvitationException;
import com.morak.back.team.exception.MismatchedTeamException;
import com.morak.back.team.ui.dto.InvitationJoinedResponse;
import com.morak.back.team.ui.dto.TeamCreateRequest;
import com.morak.back.team.ui.dto.TeamResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("?????? ????????????.")
    @Test
    void createTeam() {
        // given
        Long memberId = 1L;
        given(teamRepository.save(any())).willReturn(new Team(1L, "test-team", "abCD1234"));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(new Member(
                        1L,
                        "test-oauth-id",
                        "test-name",
                        "https://image-url.com"
                )));
        TeamCreateRequest request = new TeamCreateRequest("test-team");

        // when
        String code = teamService.createTeam(memberId, request);

        // then
        assertThat(code).isEqualTo("abCD1234");
    }

    @DisplayName("?????? ??????????????? ????????????.")
    @Test
    public void createInvitationCode() {
        // given
        Team team = new Team(1L, "test-name", "testcode");
        TeamInvitation teamInvitation = new TeamInvitation(
                null,
                team,
                InvitationCode.generate((length) -> "ABCDE12345"),
                ExpiredTime.withMinute(30L)
        );
        given(teamRepository.findByCode(anyString())).willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        given(teamInvitationRepository.save(any())).willReturn(teamInvitation);

        // when
        String code = teamService.createInvitationCode(1L, team.getCode());

        // then
        assertThat(code).isEqualTo("ABCDE12345");
    }

    @DisplayName("?????? ?????? ????????? ????????????.")
    @Test
    void isJoined() {
        // given
        Team team = new Team(1L, "test-name", "testcode");
        TeamInvitation teamInvitation = new TeamInvitation(1L, team, InvitationCode.generate((length) -> "inviteCode"),
                ExpiredTime.withMinute(30L));
        given(teamInvitationRepository.findByCode(anyString())).willReturn(Optional.of(teamInvitation));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // when
        InvitationJoinedResponse invitationJoinedResponse = teamService.isJoined(1L, "inviteCode");

        // then
        Assertions.assertAll(
                () -> assertThat(invitationJoinedResponse.getIsJoined()).isTrue(),
                () -> assertThat(invitationJoinedResponse.getName()).isEqualTo("test-name"),
                () -> assertThat(invitationJoinedResponse.getGroupCode()).isEqualTo("testcode")
        );
    }

    @DisplayName("????????? ????????????.")
    @Test
    public void joinTeam() {
        // given
        Team team = new Team(1L, "test-team", "testcode");
        given(teamInvitationRepository.findByCode(anyString())).willReturn(
                Optional.of(new TeamInvitation(null, team, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(30L))));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(false);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(new Member()));
        given(teamMemberRepository.save(any())).willReturn(any());

        // when
        String teamCode = teamService.join(1L, "invitecode");

        // then
        assertThat(teamCode).isEqualTo(team.getCode());
    }

    @DisplayName("????????? ?????? ????????? ?????? ????????? ?????????..")
    @Test
    public void throwExceptionWhenAlreadyJoined() {
        // given
        Team team = new Team(1L, "test-team", "testcode");
        given(teamInvitationRepository.findByCode(anyString())).willReturn(
                Optional.of(new TeamInvitation(null, team, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(30L))));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> teamService.join(1L, "invitecode"))
                .isInstanceOf(AlreadyJoinedTeamException.class);
    }

    @DisplayName("??????????????? ????????? ?????? ????????? ?????????.")
    @Test
    void throwsExceptionWithExpiredInvitationCode() {
        // given
        Team team = new Team(1L, "test-team", "testcode");
        given(teamInvitationRepository.findByCode(anyString())).willReturn(
                Optional.of(new TeamInvitation(null, team, InvitationCode.generate((length) -> "invitecode"), ExpiredTime.withMinute(0L))));

        // when & then
        assertThatThrownBy(() -> teamService.join(1L, "invitecode"))
                .isInstanceOf(ExpiredInvitationException.class);
    }

    @DisplayName("?????? ????????? ????????????")
    @Test
    void findTeams() {
        // given
        given(teamMemberRepository.findAllByMemberId(anyLong()))
                .willReturn(List.of(
                        new TeamMember(1L, new Team(null, "team-A", "testcode"), null),
                        new TeamMember(2L, new Team(null, "team-B", "testcoed"), null)
                ));

        // when
        List<TeamResponse> teamResponses = teamService.findTeams(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(teamResponses).hasSize(2),
                () -> assertThat(teamResponses).extracting("name", "code").containsExactly(
                        tuple("team-A", "testcode"),
                        tuple("team-B", "testcoed")
                )
        );
    }

    @DisplayName("????????? ?????? ?????? ????????? ????????????.")
    @Test
    public void findMembersInTeam() {
        // given
        Team team = new Team(1L, "name", "testcode");
        Member member1 = new Member(1L, null, "1", "123");
        Member member2 = new Member(2L, null, "2", "234");
        TeamMember teamMember1 = new TeamMember(null, team, member1);
        TeamMember teamMember2 = new TeamMember(null, team, member2);

        given(teamRepository.findByCode(anyString()))
                .willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong()))
                .willReturn(true);
        given(teamMemberRepository.findAllByTeamId(anyLong()))
                .willReturn(List.of(teamMember1, teamMember2));

        // when
        List<MemberResponse> memberResponses = teamService.findMembersInTeam(member1.getId(), "testcode");

        // then
        assertThat(memberResponses).extracting("id", "name", "profileUrl")
                .containsExactly(
                        tuple(member1.getId(), member1.getName(), member1.getProfileUrl()),
                        tuple(member2.getId(), member2.getName(), member2.getProfileUrl())
                );
    }

    @DisplayName("????????? ?????? ?????? ?????? ??? ????????? ????????? ???????????? ????????? ?????????.")
    @Test
    public void throwExceptionWhenNotJoinedTeam() {
        // given
        Team team = new Team(1L, "name", "testcode");
        Member member2 = new Member(2L, null, "2", "234");

        given(teamRepository.findByCode(anyString()))
                .willReturn(Optional.of(team));
        given(teamMemberRepository.existsByTeamIdAndMemberId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> teamService.findMembersInTeam(member2.getId(), "testcode"))
                .isInstanceOf(MismatchedTeamException.class);
    }

    @DisplayName("????????? ???????????? ????????????.")
    @Test
    void exitMemberInTeam() {
        // given
        Team team = new Team(1L, "test-team", "testcode");
        given(teamRepository.findByCode(anyString()))
                .willReturn(Optional.of(team));
        given(teamMemberRepository.findByTeamIdAndMemberId(anyLong(), anyLong()))
                .willReturn(
                        Optional.of(new TeamMember(1L, team, new Member(1L, "test-oauth", "test-member", "test-url"))));
        // when
        teamService.exitMemberFromTeam(1L, "testcode");

        // then
        verify(teamMemberRepository).delete(any());
    }

    @DisplayName("???????????? ?????? ???????????? ???????????? ????????? ?????????.")
    @Test
    void throwsExceptionWhenNotJoined() {
        // given
        Team team = new Team(1L, "test-team", "testcode");
        given(teamRepository.findByCode(anyString()))
                .willReturn(Optional.of(team));
        given(teamMemberRepository.findByTeamIdAndMemberId(anyLong(), anyLong()))
                .willThrow(MismatchedTeamException.class);

        // when & then
        assertThatThrownBy(() -> teamService.exitMemberFromTeam(1L, "testcode"))
                .isInstanceOf(MismatchedTeamException.class);
    }

    @DisplayName("????????? ????????? ?????????.")
    @Test
    void findDefaultGroup() {
        // given
        given(teamMemberRepository.findAllByMemberId(anyLong()))
                .willReturn(List.of(
                        new TeamMember(1L, new Team(null, "team-A", "testcode"), null),
                        new TeamMember(2L, new Team(null, "team-B", "testcoed"), null)
                ));
        // when
        TeamResponse defaultTeamResponse = teamService.findDefaultTeam(1L);
        // then
        assertThat(defaultTeamResponse.getName()).isEqualTo("team-A");
    }

    @DisplayName("????????? ???????????? ?????? ?????? ????????? ?????????")
    @Test
    void throwsExceptionWhenNotJoinedTeam() {
        // given
        given(teamMemberRepository.findAllByMemberId(anyLong())).willReturn(List.of());
        // when & then
        assertThatThrownBy(() -> teamService.findDefaultTeam(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}