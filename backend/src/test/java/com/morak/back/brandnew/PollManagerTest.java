package com.morak.back.brandnew;

import static com.morak.back.brandnew.DateTimeFixture.TIME_OF_2022_05_12_12_00;
import static com.morak.back.brandnew.DateTimeFixture.TIME_OF_2022_05_12_12_30;
import static com.morak.back.brandnew.MemberFixture.에덴;
import static com.morak.back.brandnew.MemberFixture.엘리;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollManagerTest {

    private Poll 문화의날_투표;
    private PollItem 볼링;
    private PollItem 보드게임;

    @BeforeEach
    void setUp() {
        문화의날_투표 = Poll.builder()
                .title("문화의날에 뭐할래?")
                .anonymous(false)
                .allowedCount(2)
                .host(엘리)
                .closedAt(FakeDateTime.builder().dateTime(TIME_OF_2022_05_12_12_30).now(TIME_OF_2022_05_12_12_00).build())
                .build();

        볼링 = PollItem.builder().subject("볼링").build();
        보드게임 = PollItem.builder().subject("보드게임").build();
    }

    @Test
    void 투표를_한다() {
        // given
        PollManager pollManager = PollManager.builder()
                .poll(문화의날_투표)
                .pollItems(List.of(볼링, 보드게임))
                .build();

        // when
        pollManager.select(에덴, Map.of(볼링, "강한 어깨"));

        // then
        Assertions.assertAll(
                () -> assertThat(pollManager.getPollItems().get(0).getSelectMembers().getMembers().size()).isEqualTo(1)
        );
    }

    @Test
    void 재투표를_한다() {
        // given
        PollManager pollManager = PollManager.builder()
                .poll(문화의날_투표)
                .pollItems(List.of(볼링, 보드게임))
                .build();

        pollManager.select(에덴, Map.of(볼링, "강한 어깨"));

        // when
        pollManager.select(에덴, Map.of(보드게임, "명석한 두뇌"));

        // then
        Assertions.assertAll(
                () -> assertThat(pollManager.getPollItems().get(0).getSelectMembers().getMembers().size()).isEqualTo(0),
                () -> assertThat(pollManager.getPollItems().get(1).getSelectMembers().getMembers().size()).isEqualTo(1)
        );
    }

    @Test
    void 투표에_참여한_멤버가_1명일_때_멤버의_수를_센다() {
        // given
        PollManager pollManager = PollManager.builder()
                .poll(문화의날_투표)
                .pollItems(List.of(볼링, 보드게임))
                .build();

        pollManager.select(에덴, Map.of(볼링, "강한 어깨"));

        // when
        int count = pollManager.countSelectMembers();

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 투표에_참여한_멤버가_2명일_때_멤버의_수를_센다() {
        // given
        PollManager pollManager = PollManager.builder()
                .poll(문화의날_투표)
                .pollItems(List.of(볼링, 보드게임))
                .build();

        pollManager.select(에덴, Map.of(볼링, "강한 어깨", 보드게임, "명석한 두뇌"));
        pollManager.select(엘리, Map.of(보드게임, "뱅 재밌어~"));

        // when
        int count = pollManager.countSelectMembers();

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void 투표를_마감한다() {
        // given
        PollManager pollManager = PollManager.builder()
                .poll(문화의날_투표)
                .pollItems(List.of(볼링, 보드게임))
                .build();

        // when
        pollManager.close(엘리);

        // then
    }
}
