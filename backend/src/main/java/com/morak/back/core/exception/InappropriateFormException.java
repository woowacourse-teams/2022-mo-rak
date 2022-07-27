package com.morak.back.core.exception;

public class InappropriateFormException extends InappropriatePropertyException {
    public InappropriateFormException(String message) {
        super(message);
    }

    public static InappropriateFormException of(String name, String property) {
        return new InappropriateFormException(
                String.format("%s의 형식이 적절하지 않습니다.(입력된 값 : %s)", name, property)
        );
    }
}
