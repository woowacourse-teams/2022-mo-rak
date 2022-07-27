package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PollResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PollItem pollItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Embedded
    @Valid
    private Description description;

    public PollResult(Long id, PollItem pollItem, Member member, String description) {
        this.id = id;
        this.pollItem = pollItem;
        this.member = member;
        this.description = new Description(description);
    }

    public PollResult fromAnonymous() {
        return new PollResult(id, pollItem, Member.getAnonymous(), description.getDescription());
    }

    public Boolean isSameMember(Member member) {
        return this.member.equals(member);
    }

    public String getDescription() {
        return description.getDescription();
    }
}
