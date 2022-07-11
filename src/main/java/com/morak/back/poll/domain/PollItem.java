package com.morak.back.poll.domain;

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

import com.morak.back.auth.domain.Member;

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

    public void addPollResult(Member member) {
        PollResult pollResult = new PollResult(null, this, member);
        pollResults.add(pollResult);
    }

    public void deleteIfPollMember(Member member) {
        pollResults.removeIf(pollResult -> pollResult.getMember().equals(member));
    }

    public List<Member> getMembersByAnonymous() {
        if (poll.getIsAnonymous()) {
            return new ArrayList<>();
        }
        return pollResults.stream()
                .map(PollResult::getMember)
                .collect(Collectors.toList());
    }
}
