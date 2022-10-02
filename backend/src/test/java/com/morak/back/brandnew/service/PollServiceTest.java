package com.morak.back.brandnew.service;

import static com.morak.back.brandnew.MemberFixture.에덴;
import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.brandnew.PollCreateRequest;
import com.morak.back.brandnew.PollResponse;
import com.morak.back.brandnew.domain.Member;
import com.morak.back.brandnew.domain.PollManager;
import com.morak.back.brandnew.repository.NewMemberRepository;
import com.morak.back.brandnew.repository.PollManagerRepository;
import com.morak.back.poll.ui.dto.PollResultRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PollServiceTest {

    private final NewMemberRepository memberRepository;
    private final PollManagerRepository pollManagerRepository;
    private final PollService pollService;

    @Autowired
    public PollServiceTest(NewMemberRepository memberRepository, PollManagerRepository pollManagerRepository) {
        this.memberRepository = memberRepository;
        this.pollManagerRepository = pollManagerRepository;
        this.pollService = new PollService(memberRepository, pollManagerRepository);
    }

    @Test
    void 투표를_생성하고_저장한다() {
        // given
        Member eden = memberRepository.save(에덴);

        List<String> subjects = List.of("볼링", "보드게임");
        PollCreateRequest request = PollCreateRequest.builder()
                .title("모락 회의")
                .anonymous(true)
                .allowedPollCount(2)
                .closedAt(LocalDateTime.now().plusDays(1))
                .subjects(subjects)
                .build();
        // when
        pollService.createPoll(eden.getId(), request);

        // then
        PollManager pollManager = pollManagerRepository.findById(1L).orElseThrow();
        assertThat(pollManager).isNotNull();
    }

    @Test
    void 코드로_투표를_조회한다() {
        // given
        Member eden = memberRepository.save(에덴);

        List<String> subjects = List.of("볼링", "보드게임");
        PollCreateRequest request = PollCreateRequest.builder()
                .title("모락 회의")
                .anonymous(true)
                .allowedPollCount(2)
                .closedAt(LocalDateTime.now().plusDays(1))
                .subjects(subjects)
                .build();

        pollService.createPoll(eden.getId(), request);

        PollManager pollManager = pollManagerRepository.findById(1L).orElseThrow();

        // when
        PollResponse response = pollService.findPoll(1L, pollManager.getPoll().getCode().getCode());

        // then
        assertThat(response.getTitle()).isEqualTo("모락 회의");
    }

    @Test
    void 투표_항목을_선택한다() {
        // given
        Member eden = memberRepository.save(에덴);

        List<String> subjects = List.of("볼링", "보드게임");
        PollCreateRequest request = PollCreateRequest.builder()
                .title("모락 회의")
                .anonymous(true)
                .allowedPollCount(2)
                .closedAt(LocalDateTime.now().plusDays(1))
                .subjects(subjects)
                .build();

        pollService.createPoll(eden.getId(), request);

        PollManager pollManager = pollManagerRepository.findById(1L).orElseThrow();

        // when
        String code = pollManager.getPoll().getCode().getCode();
        List<PollResultRequest> requests = List.of(new PollResultRequest(1L, "그냥!"));
        pollService.selectPollItems(1L, code, requests);

        // then
        PollManager foundPollManager = pollManagerRepository.findByCode(code).orElseThrow();
        assertThat(foundPollManager.getPollItems().getValues().get(0).getSelectMembers().getValues().get(에덴)).isEqualTo(
                "그냥!");
    }

    @Test
    void 투표_항목을_재선택한다() {
        // given
        Member eden = memberRepository.save(에덴);

        List<String> subjects = List.of("볼링", "보드게임");
        PollCreateRequest request = PollCreateRequest.builder()
                .title("모락 회의")
                .anonymous(true)
                .allowedPollCount(2)
                .closedAt(LocalDateTime.now().plusDays(1))
                .subjects(subjects)
                .build();

        pollService.createPoll(eden.getId(), request);

        PollManager pollManager = pollManagerRepository.findById(1L).orElseThrow();

        // when
        String code = pollManager.getPoll().getCode().getCode();
        List<PollResultRequest> requests = List.of(new PollResultRequest(1L, "그냥!"));
        pollService.selectPollItems(1L, code, requests);
        List<PollResultRequest> boardRequests = List.of(new PollResultRequest(2L, "볼링 비싸요!"));
        pollService.selectPollItems(1L, code, boardRequests);

        // then
        PollManager foundPollManager = pollManagerRepository.findByCode(code).orElseThrow();
        Assertions.assertAll(
                () -> assertThat(foundPollManager.getPollItems().getValues().get(0).getSelectMembers().getValues().get(에덴)).isNull(),
                () -> assertThat(foundPollManager.getPollItems().getValues().get(1).getSelectMembers().getValues().get(에덴)).isEqualTo("볼링 비싸요!")
        );
    }
}
