package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.Team;
import com.morak.back.poll.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Poll extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    private String title;

    private Integer allowedPollCount;

    private Boolean isAnonymous;

    @Enumerated(value = EnumType.STRING)
    private PollStatus status;

    private LocalDateTime closedAt;

    private String code;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<PollItem> pollItems = new ArrayList<>();

    public Poll(Long id, Team team, Member host, String title, Integer allowedPollCount, Boolean isAnonymous,
                PollStatus status, LocalDateTime closedAt, String code) {

        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.allowedPollCount = allowedPollCount;
        this.isAnonymous = isAnonymous;
        this.status = status;
        this.closedAt = closedAt;
        this.code = code;
    }

    public void addItem(PollItem pollItem) {
        pollItems.add(pollItem);
    }

    public void doPoll(List<PollItem> newItems, Member member) {
        validateStatus();
        validateCounts(newItems.size());
        validateNewItemsBelongsTo(newItems);
        deleteMembersFromPollItems(member);
        addMembersToPollItems(newItems, member);
    }

    private void validateStatus() {
        if (status.isClosed()) {
            throw new InvalidRequestException();
        }
    }

    private void validateCounts(int itemSize) {
        if (itemSize > allowedPollCount || itemSize == 0) {
            throw new InvalidRequestException();
        }
    }

    private void validateNewItemsBelongsTo(List<PollItem> newItems) {
        if (!this.pollItems.containsAll(newItems)) {
            throw new InvalidRequestException();
        }
    }

    private void deleteMembersFromPollItems(Member member) {
        for (PollItem pollItem : pollItems) {
            pollItem.deleteIfPollMember(member);
        }
    }

    private void addMembersToPollItems(List<PollItem> newItems, Member member) {
        for (PollItem newItem : newItems) {
            newItem.addPollResult(member);
        }
    }

    public boolean isHost(Member member) {
        return host.equals(member);
    }

    public void validateHost(Member member) {
        if (!this.host.equals(member)) {
            throw new InvalidRequestException();
        }
    }

    public void close(Member member) {
        validateHost(member);
        status = status.close();
    }
}
