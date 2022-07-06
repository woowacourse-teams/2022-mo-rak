package com.morak.back.poll.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morak.back.poll.application.PollService;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private Long tempMemberId = 1L;
    private Long tempTeamId = 1L;

    @PostMapping
    public ResponseEntity<Void> createPoll(@Valid @RequestBody PollCreateRequest request) {
        Long id = pollService.createPoll(tempTeamId, tempMemberId, request);
        return ResponseEntity.created(URI.create("/polls/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> doPoll(@PathVariable Long id, @RequestBody List<Long> itemIds) {
        pollService.doPoll(tempMemberId, id, itemIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> findPolls() {
        return ResponseEntity.ok(pollService.findPolls(tempTeamId, tempMemberId));
    }

    @GetMapping("/{id}")
    public void findPoll(@PathVariable Long id) {

    }

    @GetMapping("/{id}/result")
    public void findPollResult(@PathVariable Long id) {

    }

    @DeleteMapping("/{id}")
    public void deletePoll(@PathVariable Long id) {

    }

    @PatchMapping("/{id}/status")
    public void closePoll(@PathVariable Long id) {

    }
}
