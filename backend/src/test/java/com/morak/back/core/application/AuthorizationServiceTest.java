package com.morak.back.core.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.MemberNotFoundException;
import com.morak.back.core.domain.Code;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.support.ServiceTest;
import com.morak.back.team.domain.Team;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import com.morak.back.team.domain.TeamRepository;
import com.morak.back.team.exception.TeamAuthorizationException;
import com.morak.back.team.exception.TeamNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AuthorizationServiceTest {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationServiceTest(TeamRepository teamRepository,
                                    MemberRepository memberRepository,
                                    TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.authorizationService = new AuthorizationService(teamRepository, memberRepository, teamMemberRepository);
    }

    private Member member;
    private Team team;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .oauthId("oauth1")
                .name("한해리")
                .profileUrl("http://haeri-profile.com")
                .build());
        team = teamRepository.save(Team.builder()
                .name("성북구 모여라")
                .code(Code.generate(length -> "abcd1234"))
                .build());
        teamMemberRepository.save(new TeamMember(null, team, member));
    }

    @Test
    void 올바른_멤버와_팀인지_검증한다() {
        // when & then
        assertDoesNotThrow(
                () -> authorizationService.withTeamMemberValidation(
                        () -> null, team.getCode(), member.getId()
                ));
    }

    @Test
    void 없는_멤어인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(
                () -> authorizationService.withTeamMemberValidation(
                        () -> null, team.getCode(), 0L
                )).isInstanceOf(MemberNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    void 없는_팀인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(
                () -> authorizationService.withTeamMemberValidation(
                        () -> null, "dddddddd", member.getId()
                )).isInstanceOf(TeamNotFoundException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_NOT_FOUND_ERROR);
    }

    @Test
    void 멤버가_팀에_속하지_않는_경우_예외를_던진다() {
        // given
        Member otherMember = memberRepository.save(Member.builder()
                .oauthId("oauth2")
                .name("김예지")
                .profileUrl("http://winnie-profile.com")
                .build());

        // when & then
        assertThatThrownBy(
                () -> authorizationService.withTeamMemberValidation(
                        () -> null, team.getCode(), otherMember.getId()
                )).isInstanceOf(TeamAuthorizationException.class)
                .extracting("code")
                .isEqualTo(CustomErrorCode.TEAM_MEMBER_MISMATCHED_ERROR);
    }
}
