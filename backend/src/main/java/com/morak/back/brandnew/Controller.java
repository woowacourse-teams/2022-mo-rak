package com.morak.back.brandnew;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private final List<PollManager> pollManagers = new ArrayList<>();

    public void createPoll(Member host, PollCreateRequest request) {
        Poll poll = Poll.builder()
                .title(request.getTitle())
                .anonymous(request.getAnonymous())
                .allowedCount(request.getAllowedPollCount())
                .host(host)
                .closedAt(new RealDateTime(request.getClosedAt()))
                .build();
        List<PollItem> pollItems = request.getSubjects().stream()
                .map(subject -> PollItem.builder().subject(subject).build())
                .collect(Collectors.toList());
        PollManager pollManager = PollManager.builder().poll(poll).pollItems(pollItems).build();
        pollManagers.add(pollManager);
    }


}
