package com.morak.back.role.domain;

import java.util.List;

public interface ShuffleStrategy {

    void shuffle(List<Long> memberIds);
}
