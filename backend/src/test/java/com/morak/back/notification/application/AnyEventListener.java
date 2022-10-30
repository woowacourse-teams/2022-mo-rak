package com.morak.back.notification.application;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnyEventListener {

    private List<Object> events = new ArrayList<>();

    @TransactionalEventListener
    public void addEvent(Object event) {
        events.add(event);
    }


    public boolean hasEvent(Class<?> eventClass) {
        return events.stream()
                .anyMatch(event -> event.getClass().isAssignableFrom(eventClass));
    }

    public void clear() {
        events.clear();
    }
}
