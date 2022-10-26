package com.morak.back.poll.application.dto;

import com.morak.back.auth.domain.Member;
import com.morak.back.poll.domain.PollItem;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollItemResultResponse {

    private Long id;
    private Integer count;
    private List<MemberResultResponse> members;
    private String subject;

    public static PollItemResultResponse of(PollItem pollItem, List<Member> members, Boolean anonymous) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.countSelectMembers(),
                toMemberResponsesByAnonymous(pollItem.getSelectMembers(), members, pickStrategy(anonymous)),
                pollItem.getSubject()
        );
    }

    private static List<MemberResultResponse> toMemberResponsesByAnonymous(Map<Long, String> selectMembers,
                                                                           List<Member> members,
                                                                           MemberViewStrategy strategy) {

        return selectMembers.entrySet().stream()
                .map(entry -> Map.entry(findMemberById(members, entry.getKey()), entry.getValue()))
                .map(entry -> MemberResultResponse.of(strategy.viewMemberFrom(entry), entry.getValue()))
                .collect(Collectors.toList());
    }

    private static Member findMemberById(List<Member> members, Long memberId) {
        return members.stream()
                .filter(member -> member.isSameId(memberId))
                .findFirst()
                .orElseThrow();
    }

    private static MemberViewStrategy pickStrategy(boolean anonymous) {
        if (anonymous) {
            return new AnonymousMemberViewStrategy();
        }
        return new NonAnonymousMemberViewStrategy();
    }

    private interface MemberViewStrategy {

        Member viewMemberFrom(Entry<Member, String> selectMemberEntry);
    }

    private static class AnonymousMemberViewStrategy implements MemberViewStrategy {

        @Override
        public Member viewMemberFrom(Entry<Member, String> selectMemberEntry) {
            return Member.getAnonymousMember();
        }
    }

    private static class NonAnonymousMemberViewStrategy implements MemberViewStrategy {

        @Override
        public Member viewMemberFrom(Entry<Member, String> selectMemberEntry) {
            return selectMemberEntry.getKey();
        }
    }
}
