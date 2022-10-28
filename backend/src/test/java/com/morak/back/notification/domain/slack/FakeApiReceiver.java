package com.morak.back.notification.domain.slack;

import com.morak.back.support.FakeBean;

@FakeBean
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
