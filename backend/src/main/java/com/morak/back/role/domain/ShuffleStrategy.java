package com.morak.back.role.domain;

import java.util.List;

public interface ShuffleStrategy {

    List<Long> shuffle(List<Long> memberIds);
}
