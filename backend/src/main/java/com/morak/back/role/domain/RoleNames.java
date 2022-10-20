package com.morak.back.role.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RoleNames {

    @ElementCollection
    @CollectionTable(name = "role_name", joinColumns = @JoinColumn(name = "role_id"))
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private List<RoleName> values = new ArrayList<>();

    public RoleNames(List<RoleName> values) {
        validateMinSize(values.size());
        this.values = values;
    }

    public static RoleNames from(List<String> values) {
        return new RoleNames(values.stream()
                .map(RoleName::new)
                .collect(Collectors.toList()));
    }

    private void validateMinSize(int size) {
        if (size == 0) {
            throw new RoleDomainLogicException(
                    CustomErrorCode.ROLE_NAMES_DEFAULT_SIZE_ERROR,
                    "역할 이름의 개수는 하나 이상이어야 합니다"
            );
        }
    }

    public List<RoleMatchResult> match(List<Long> memberIds) {
        validateMemberCount(memberIds.size());
        return IntStream.range(0, this.values.size())
                .mapToObj(index -> new RoleMatchResult(this.values.get(index), memberIds.get(index)))
                .collect(Collectors.toList());
    }

    private void validateMemberCount(int size) {
        if (size < this.values.size()) {
            throw new RoleDomainLogicException(
                    CustomErrorCode.ROLE_NAMES_MAX_SIZE_ERROR,
                    "멤버의 개수(" + size + ")보다 역할의 개수(" + +this.values.size() + ")가 더 적어야 합니다");
        }
    }
}
