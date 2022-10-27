package com.morak.back.poll.domain;

import com.morak.back.core.support.Generated;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Subject subject;

    @ElementCollection
    @CollectionTable(
            name = "select_member",
            joinColumns = @JoinColumn(name = "poll_item_id")
    )
    @MapKeyColumn(name = "member_id")
    @Column(name = "description", nullable = false)
    private Map<Long, Description> selectMembers = new HashMap<>();

    public PollItem(Long id) {
        this(id, null, new HashMap<>());
    }

    @Builder
    public PollItem(Long id, String subject) {
        this(id, new Subject(subject), new HashMap<>());
    }

    private PollItem(Long id, Subject subject, Map<Long, Description> selectMembers) {
        this.id = id;
        this.subject = subject;
        this.selectMembers = selectMembers;
    }

    public Set<Long> getOnlyMembers() {
        return selectMembers.keySet();
    }

    public void addSelectMember(Long memberId, String description) {
        selectMembers.put(memberId, new Description(description));
    }

    public void remove(Long memberId) {
        selectMembers.remove(memberId);
    }

    public int countSelectMembers() {
        return selectMembers.size();
    }

    public Boolean isSelectedBy(Long memberId) {
        return selectMembers.containsKey(memberId);
    }

    public String getDescriptionFrom(Long memberId) {
        Optional<Description> description = Optional.ofNullable(selectMembers.get(memberId));

        if (description.isPresent()) {
            return description.get().getValue();
        }

        return "";
    }

    public String getSubject() {
        return this.subject.getValue();
    }

    public Map<Long, String> getSelectMembers() {
        return this.selectMembers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PollItem)) {
            return false;
        }
        PollItem that = (PollItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(getId());
    }
}
