package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import com.morak.back.core.domain.Code;
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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @NotNull(message = "team 은 null 일 수 없습니다.")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "host 는 null 일 수 없습니다.")
    private Member host;

    @NotBlank(message = "title 은 blank 일 수 없습니다.")
    @Size(min = 1, max = 255, message = "제목의 길이는 1 ~ 255자여야합니다.")
    private String title;

    @Embedded
    @Valid
    private AllowedPollCount allowedPollCount;

    @NotNull(message = "익명여부는 null이 아니어야합니다.")
    private Boolean isAnonymous;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "status 는 null 일 수 없습니다.")
    private PollStatus status;

    @NotNull(message = "closedAt 은 null 일 수 없습니다.")
    private LocalDateTime closedAt;

    @Embedded
    @Valid
    private Code code;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<PollItem> pollItems = new ArrayList<>();

    @Builder
    private Poll(Long id, Team team, Member host, String title, Integer allowedPollCount, Boolean isAnonymous,
                PollStatus status, LocalDateTime closedAt, Code code) {

        this.id = id;
        this.team = team;
        this.host = host;
        this.title = title;
        this.allowedPollCount = new AllowedPollCount(allowedPollCount);
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

    private void validateCounts(Integer pollItemCount) {
        if (!allowedPollCount.isAllowed(pollItemCount)) {
            throw new InvalidRequestException(id + "번 투표에 " + pollItemCount + "개의 투표 항목을 선택할 수 없습니다.");
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

    public Integer getAllowedPollCount() {
        return allowedPollCount.getAllowedPollCount();
    }

    public String getCode() {
        return code.getCode();
    }
}
