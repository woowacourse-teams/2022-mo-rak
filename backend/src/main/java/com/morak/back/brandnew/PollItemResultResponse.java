package com.morak.back.brandnew;


import com.morak.back.auth.domain.Member;
import com.morak.back.brandnew.domain.NewPollItem;
import com.morak.back.brandnew.domain.SelectMembers;
import com.morak.back.poll.ui.dto.MemberResultResponse;
import java.util.List;
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

    public static PollItemResultResponse of(NewPollItem pollItem, Boolean anonymous) {
        return new PollItemResultResponse(
                pollItem.getId(),
                pollItem.getOnlyMembers().size(),
                toMemberResponsesByAnonymous(pollItem.getSelectMembers(), pickStrategy(anonymous)),
                pollItem.getSubject()
        );
    }

    private static List<MemberResultResponse> toMemberResponsesByAnonymous(SelectMembers selectMembers,
                                                                           MemberViewStrategy strategy) {
        return selectMembers.getValues().entrySet().stream()
                .map(entry -> MemberResultResponse.of(strategy.viewMemberFrom(entry), entry.getValue()))
                .collect(Collectors.toList());
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
            return Member.getAnonymous();
        }
    }

    private static class NonAnonymousMemberViewStrategy implements MemberViewStrategy {

        @Override
        public Member viewMemberFrom(Entry<Member, String> selectMemberEntry) {
            return selectMemberEntry.getKey();
        }
    }
}
