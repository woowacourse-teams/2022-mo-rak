package com.morak.back.role.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class RoleNames {

    private static final int MAX_SIZE = 100;

    private final List<RoleName> values;

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

    public Map<RoleName, Long> match(List<Long> memberIds) {
        validateMemberCount(memberIds.size());
        return IntStream.range(0, values.size())
                .boxed()
                .collect(Collectors.toMap(
                        values::get,
                        memberIds::get
                ));
    }

    private void validateMemberCount(int size) {
        if (size < this.values.size()) {
            throw new RoleDomainLogicException(
                    CustomErrorCode.ROLE_NAMES_MAX_SIZE_ERROR,
                    "멤버의 개수(" + size + ")보다 역할의 개수(" + +this.values.size() + ")가 더 적어야 합니다");
        }
    }
}
