package com.morak.back.team.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.TeamMember;
import com.morak.back.auth.domain.TeamMemberRepository;
import com.morak.back.poll.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.core.util.CodeGenerator;
import com.morak.back.team.ui.dto.TeamCreateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private static final int CODE_LENGTH = 8;

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;

    public String createTeam(Long memberId, TeamCreateRequest request) {
        Team team = new Team(null, request.getName(), CodeGenerator.createRandomCode(CODE_LENGTH));
        Member member = memberRepository.findById(memberId).orElseThrow();
        Team savedTeam = teamRepository.save(team);

        TeamMember teamMember = new TeamMember(null, savedTeam, member);
        teamMemberRepository.save(teamMember);

        return savedTeam.getCode();
    }
}
