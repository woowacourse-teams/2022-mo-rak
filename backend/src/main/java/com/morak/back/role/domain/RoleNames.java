package com.morak.back.role.domain;

import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.role.exception.RoleDomainLogicException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class RoleNames {

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
                    CustomErrorCode.INVALID_ROLE_NAMES_SIZE_ERROR,
                    "역할 이름의 개수는 하나 이상이어야 합니다"
            );
        }
    }
}
