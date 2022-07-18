package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PollItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Poll poll;

    private String subject;

    @OneToMany(mappedBy = "pollItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollResult> pollResults = new ArrayList<>();

    public PollItem(Long id, Poll poll, String subject) {
        this.id = id;
        this.poll = poll;
        this.subject = subject;
    }

    public void addPollResult(Member member, String description) {
        PollResult pollResult = new PollResult(null, this, member, description);
        pollResults.add(pollResult);
    }

    public void deletePollResultIfPollMember(Member member) {
        pollResults.removeIf(pollResult -> pollResult.getMember().equals(member));
    }

    public List<PollResult> getResultsByAnonymous() {
        if (poll.getIsAnonymous()) {
            return pollResults.stream()
                    .map(PollResult::fromAnonymous)
                    .collect(Collectors.toList());
        }
        return pollResults;
    }
}
