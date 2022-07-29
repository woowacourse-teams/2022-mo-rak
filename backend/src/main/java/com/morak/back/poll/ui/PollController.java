package com.morak.back.poll.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollResultRequest;
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
@RequestMapping("/api/groups/{groupCode}/polls")
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<Void> createPoll(@PathVariable String groupCode,
                                           @Auth Long memberId,
                                           @Valid @RequestBody PollCreateRequest request) {
        Long id = pollService.createPoll(groupCode, memberId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/polls/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> doPoll(@PathVariable String groupCode,
                                       @Auth Long memberId,
                                       @PathVariable Long id,
                                       @Valid @RequestBody List<PollResultRequest> requests) {
        pollService.doPoll(groupCode, memberId, id, requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> findPolls(@PathVariable String groupCode,
                                                        @Auth Long memberId) {
        return ResponseEntity.ok(pollService.findPolls(groupCode, memberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponse> findPoll(@PathVariable String groupCode,
                                                 @Auth Long memberId,
                                                 @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPoll(groupCode, memberId, id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<PollItemResponse>> findPollItems(@PathVariable String groupCode,
                                                                @Auth Long memberId,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPollItems(groupCode, memberId, id));
    }


    @GetMapping("/{id}/result")
    public ResponseEntity<List<PollItemResultResponse>> findPollResult(@PathVariable String groupCode,
                                                                       @Auth Long memberId,
                                                                       @PathVariable Long id) {
        return ResponseEntity.ok(pollService.findPollItemResults(groupCode, memberId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable String groupCode,
                                           @Auth Long memberId,
                                           @PathVariable Long id) {
        pollService.deletePoll(groupCode, memberId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> closePoll(@PathVariable String groupCode,
                                          @Auth Long memberId,
                                          @PathVariable Long id) {
        pollService.closePoll(groupCode, memberId, id);
        return ResponseEntity.ok().build();
    }
}
