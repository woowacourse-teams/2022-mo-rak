package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.application.AuthorizationService;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.core.support.Generated;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollNotFoundException;
import com.morak.back.team.domain.TeamMember;
import com.morak.back.team.domain.TeamMemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PollService {

    private final PollRepository pollRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final AuthorizationService authorizationService;

    public PollResponse createPoll(String teamCode, Long memberId, PollCreateRequest request) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = request.toPoll(teamCode, memberId);
                    return PollResponse.from(memberId, pollRepository.save(poll));
                }, teamCode, memberId
        );
    }

    @Transactional(readOnly = true)
    public PollResponse findPoll(String teamCode, Long memberId, String pollCode) {
        return authorizationService.withTeamMemberValidation(
                () -> PollResponse.from(memberId, findPollInTeam(teamCode, pollCode)), teamCode, memberId
        );
    }

    public void doPoll(String teamCode, Long memberId, String pollCode, List<PollResultRequest> requests) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = findPollInTeam(teamCode, pollCode);
                    poll.doPoll(memberId, toDataOfSelected(requests));
                    return null;
                }, teamCode, memberId
        );
    }

    private Map<PollItem, String> toDataOfSelected(List<PollResultRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(PollResultRequest::toPollItem, PollResultRequest::getDescription));
    }

    @Transactional(readOnly = true)
    public List<PollResponse> findPolls(String teamCode, Long memberId) {
        return authorizationService.withTeamMemberValidation(
                () -> pollRepository.findAllByTeamCode(teamCode)
                        .stream()
                        .map(poll -> PollResponse.from(memberId, poll))
                        .sorted()
                        .collect(Collectors.toList()), teamCode, memberId
        );
    }

    @Transactional(readOnly = true)
    public List<PollItemResultResponse> findPollResults(String teamCode, Long memberId, String pollCode) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = findPollInTeam(teamCode, pollCode);
                    List<Member> members = findMembersByTeamCode(teamCode);

                    return poll.getPollItems()
                            .stream()
                            .map(pollItem -> PollItemResultResponse.of(pollItem, members, poll.isAnonymous()))
                            .collect(Collectors.toList());
                }, teamCode, memberId
        );
    }

    private List<Member> findMembersByTeamCode(String teamCode) {
        return teamMemberRepository.findAllByTeamCode(teamCode)
                .stream()
                .map(TeamMember::getMember)
                .collect(Collectors.toList());
    }

    public void deletePoll(String teamCode, Long memberId, String pollCode) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = findPollInTeam(teamCode, pollCode);
                    validateHost(memberId, poll);
                    pollRepository.delete(poll);
                    return null;
                }, teamCode, memberId
        );
    }

    private void validateHost(Long memberId, Poll poll) {
        if (!poll.isHost(memberId)) {
            throw new PollAuthorizationException(
                    CustomErrorCode.POLL_HOST_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + poll.getCode() + " 코드 투표의 호스트가 아닙니다."
            );
        }
    }

    public void closePoll(String teamCode, Long memberId, String pollCode) {
        authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = findPollInTeam(teamCode, pollCode);
                    poll.close(memberId);
                    pollRepository.save(poll);
                    return null;
                }, teamCode, memberId
        );
    }

    @Transactional(readOnly = true)
    public List<PollItemResponse> findPollItems(String teamCode, Long memberId, String pollCode) {
        return authorizationService.withTeamMemberValidation(
                () -> {
                    Poll poll = pollRepository.findFetchedByCode(pollCode).orElseThrow(
                            () -> PollNotFoundException.of(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode)
                    );
                    validatePollInTeam(teamCode, poll);

                    return poll.getPollItems()
                            .stream()
                            .map(pollItem -> PollItemResponse.from(pollItem, memberId))
                            .collect(Collectors.toList());
                }, teamCode, memberId
        );
    }

    @Generated
    public void closeAllBeforeNow() {
        List<Poll> pollsToBeClosed = pollRepository.findAllToBeClosed(LocalDateTime.now());
        for (Poll poll : pollsToBeClosed) {
            poll.close(poll.getHostId());
            pollRepository.save(poll);
        }
    }

    private Poll findPollInTeam(String teamCode, String pollCode) {
        Poll poll = pollRepository.findByCode(pollCode)
                .orElseThrow(() -> PollNotFoundException.of(CustomErrorCode.POLL_NOT_FOUND_ERROR, pollCode));
        validatePollInTeam(teamCode, poll);
        return poll;
    }

    private void validatePollInTeam(String teamCode, Poll poll) {
        if (!poll.isBelongedTo(teamCode)) {
            throw new PollAuthorizationException(CustomErrorCode.POLL_TEAM_MISMATCHED_ERROR,
                    poll.getCode() + " 코드의 투표는 " + teamCode + " 코드의 팀에 속해있지 않습니다."
            );
        }
    }
}
