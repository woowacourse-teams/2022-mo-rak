package com.morak.back.core.exception;

public class InappropriateLengthException extends InappropriatePropertyException {
    public InappropriateLengthException(String message) {
        super(message);
    }

    public static InappropriateLengthException of(String name, String property) {
        return new InappropriateLengthException(
                String.format("%s의 길이가 적절하지 않습니다.(입력된 값 : %s)", name, property)
        );
    }
}
