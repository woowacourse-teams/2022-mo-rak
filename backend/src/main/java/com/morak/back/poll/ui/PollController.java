package com.morak.back.poll.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private Long tempTeamId = 1L;

    @PostMapping
    public ResponseEntity<Void> createPoll(@Auth Long memberId, @Valid @RequestBody PollCreateRequest request) {
        Long id = pollService.createPoll(tempTeamId, memberId, request);
        return ResponseEntity.created(URI.create("/polls/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> doPoll(@Auth Long memberId, @PathVariable Long id,
                                       @Valid @RequestBody List<PollItemRequest> requests) {
        pollService.doPoll(memberId, id, requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> findPolls(@Auth Long memberId) {
        return ResponseEntity.ok(pollService.findPolls(tempTeamId, memberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponse> findPoll(@Auth Long memberId, @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPoll(tempTeamId, memberId, id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<PollItemResponse>> findPollItems(@Auth Long memberId, @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPollItems(tempTeamId, memberId, id));
    }


    @GetMapping("/{id}/result")
    public ResponseEntity<List<PollItemResultResponse>> findPollResult(@Auth Long memberId, @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPollItemResults(tempTeamId, memberId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@Auth Long memberId, @PathVariable Long id) {
        pollService.deletePoll(tempTeamId, memberId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> closePoll(@Auth Long memberId, @PathVariable Long id) {
        pollService.closePoll(tempTeamId, memberId, id);
        return ResponseEntity.ok().build();
    }
}
