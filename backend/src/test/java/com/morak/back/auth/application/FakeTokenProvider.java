package com.morak.back.auth.application;

import com.morak.back.support.FakeBean;

@FakeBean
public class FakeTokenProvider extends JwtTokenProvider {

    public FakeTokenProvider() {
        super("9875a0b4ee6605257509be56c0c0db8ac7657c56e008b2d0087efece6e0accd8", 3600000L);
    }
}
