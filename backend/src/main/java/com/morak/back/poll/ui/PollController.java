package com.morak.back.poll.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.poll.application.PollService;
import com.morak.back.poll.application.dto.PollCreateRequest;
import com.morak.back.poll.application.dto.PollItemResponse;
import com.morak.back.poll.application.dto.PollItemResultResponse;
import com.morak.back.poll.application.dto.PollResponse;
import com.morak.back.poll.application.dto.PollResultRequest;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping("/api/groups/{groupCode}/polls")
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@PathVariable String groupCode,
                                                   @Auth Long memberId,
                                                   @Valid @RequestBody PollCreateRequest request) {
        PollResponse pollResponse = pollService.createPoll(groupCode, memberId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/polls/" + pollResponse.getCode()))
                .body(pollResponse);
    }

    @PutMapping("/{pollCode}")
    public ResponseEntity<Void> doPoll(@PathVariable String groupCode,
                                       @Auth Long memberId,
                                       @PathVariable String pollCode,
                                       @Valid @RequestBody List<PollResultRequest> requests) {
        pollService.doPoll(groupCode, memberId, pollCode, requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> findPolls(@PathVariable String groupCode, @Auth Long memberId) {
        return ResponseEntity.ok(pollService.findPolls(groupCode, memberId));
    }

    @GetMapping("/{pollCode}")
    public ResponseEntity<PollResponse> findPoll(@PathVariable String groupCode,
                                                 @Auth Long memberId,
                                                 @PathVariable String pollCode) {
        return ResponseEntity.ok(pollService.findPoll(groupCode, memberId, pollCode));
    }

    @GetMapping("/{pollCode}/items")
    public ResponseEntity<List<PollItemResponse>> findPollItems(@PathVariable String groupCode,
                                                                @Auth Long memberId,
                                                                @PathVariable String pollCode) {
        return ResponseEntity.ok(pollService.findPollItems(groupCode, memberId, pollCode));
    }


    @GetMapping("/{pollCode}/result")
    public ResponseEntity<List<PollItemResultResponse>> findPollResult(@PathVariable String groupCode,
                                                                       @Auth Long memberId,
                                                                       @PathVariable String pollCode) {
        return ResponseEntity.ok(pollService.findPollResults(groupCode, memberId, pollCode));
    }

    @DeleteMapping("/{pollCode}")
    public ResponseEntity<Void> deletePoll(@PathVariable String groupCode,
                                           @Auth Long memberId,
                                           @PathVariable String pollCode) {
        pollService.deletePoll(groupCode, memberId, pollCode);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{pollCode}/close")
    public ResponseEntity<Void> closePoll(@PathVariable String groupCode,
                                          @Auth Long memberId,
                                          @PathVariable String pollCode) {
        pollService.closePoll(groupCode, memberId, pollCode);
        return ResponseEntity.ok().build();
    }
}
