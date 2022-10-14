package com.morak.back.role.domain;

import java.util.Collections;
import java.util.List;

public class RandomShuffleStrategy implements ShuffleStrategy {

    @Override
    public void shuffle(List<Long> memberIds) {
        Collections.shuffle(memberIds);
    }
}
