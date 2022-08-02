package com.morak.back.appointment;

import com.morak.back.core.domain.CodeGenerator;

public class FakeCodeGenerator implements CodeGenerator {


    @Override
    public String generate(int length) {
        return "FJn3ND26";
    }
}
