package com.morak.back.core.domain.slack;

import org.springframework.stereotype.Component;

@Component
public class FakeApiReceiver {

    private String message;

    public FakeApiReceiver() {
    }

    public String getMessage() {
        String returnMessage = message;
        message = "";
        return returnMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
