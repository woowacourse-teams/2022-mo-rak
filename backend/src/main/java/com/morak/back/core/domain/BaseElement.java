package com.morak.back.core.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseElement {

    private LocalDateTime createdAt;

    public BaseElement() {
        this.createdAt = LocalDateTime.now();
    }
}
