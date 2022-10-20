package com.morak.back.role.domain;

import com.morak.back.core.support.Generated;
import java.util.Collections;
import java.util.List;

public class RandomShuffleStrategy implements ShuffleStrategy {

    @Override
    @Generated
    public void shuffle(List<Long> memberIds) {
        Collections.shuffle(memberIds);
    }
}
