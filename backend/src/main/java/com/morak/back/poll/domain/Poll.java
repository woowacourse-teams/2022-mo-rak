package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.exception.InvalidRequestException;
import com.morak.back.team.domain.Team;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
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

    public void doPoll(Member member, Map<PollItem, String> mappedItemAndDescription) {
        validateStatus();
        validateCounts(mappedItemAndDescription.size());
        validateNewItemsBelongsTo(mappedItemAndDescription.keySet());

        deleteMembersFromPollItems(member);
        addMembersToPollItems(member, mappedItemAndDescription);
    }

    private void validateStatus() {
        if (status.isClosed()) {
            throw new InvalidRequestException(id + "번 투표는 종료되었습니다.");
        }
    }

    private void validateCounts(int itemSize) {
        if (itemSize > allowedPollCount || itemSize == 0) {
            throw new InvalidRequestException(id + "번 투표에 " + itemSize + "개의 투표 항목을 선택할 수 없습니다.");
        }
    }

    private void validateNewItemsBelongsTo(Set<PollItem> newItems) {
        if (!this.pollItems.containsAll(newItems)) {
            throw new InvalidRequestException(
                    id + "번 투표에 " +
                            newItems.stream()
                                    .map(pollItem -> pollItem.getId().toString())
                                    .collect(Collectors.joining(", ")) + "번 항목들은 투표할 수 없습니다.");
        }
    }

    private void deleteMembersFromPollItems(Member member) {
        for (PollItem pollItem : pollItems) {
            pollItem.deletePollResultIfPollMember(member);
        }
    }

    private void addMembersToPollItems(Member member, Map<PollItem, String> mappedItemAndDescription) {
        for (Entry<PollItem, String> entry : mappedItemAndDescription.entrySet()) {
            entry.getKey().addPollResult(member, entry.getValue());
        }
    }

    public boolean isHost(Member member) {
        return host.equals(member);
    }

    public void close(Member member) {
        validateHost(member);
        status = status.close();
    }

    public void validateHost(Member member) {
        if (!this.host.equals(member)) {
            throw new InvalidRequestException(member.getId() + "번 멤버는 " + id + "번 투표의 호스트가 아닙니다.");
        }
    }
}
