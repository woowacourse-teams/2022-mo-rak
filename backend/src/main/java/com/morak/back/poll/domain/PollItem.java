package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PollItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Poll poll;

    @NotEmpty(message = "subject 는 null 일 수 없습니다.")
    @Size(min = 1, max = 255, message = "투표 항목 주제의 길이는 1~255자여아 합니다.")
    private String subject;

    @OneToMany(mappedBy = "pollItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollResult> pollResults = new ArrayList<>();

    @Builder
    public PollItem(Long id, Poll poll, String subject) {
        this.id = id;
        this.poll = poll;
        this.subject = subject;
    }

    public void addPollResult(Member member, String description) {
        PollResult pollResult = PollResult.builder()
                .pollItem(this)
                .member(member)
                .description(description)
                .build();
        pollResults.add(pollResult);
    }

    public void deletePollResultIfPollMember(Member member) {
        pollResults.removeIf(pollResult -> pollResult.isSameMember(member));
    }

    public List<PollResult> getResultsByAnonymous() {
        if (poll.getIsAnonymous()) {
            return pollResults.stream()
                    .map(PollResult::fromAnonymous)
                    .collect(Collectors.toList());
        }
        return pollResults;
    }

    public Boolean isSelectedBy(Member member) {
        return pollResults.stream()
                .anyMatch(result -> result.isSameMember(member));
    }

    public String getDescriptionFrom(Member member) {
        return pollResults.stream()
                .filter(result -> result.isSameMember(member))
                .map(PollResult::getDescription)
                .findFirst()
                .orElseGet(() -> "");
    }
}
