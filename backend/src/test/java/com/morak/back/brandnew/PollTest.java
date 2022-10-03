package com.morak.back.brandnew;

class PollTest {

//    @Test
//    void 투표_생성시_마감_시간이_현재보다_이전이면_예외를_던진다() {
//        // given
//
//        MorakDateTime closedAt = FakeDateTime.builder()
//                .dateTime(TIME_OF_2022_05_12_12_00)
//                .now(TIME_OF_2022_05_12_12_30)
//                .build();
//
//        // when & then
//        assertThatThrownBy(() -> Poll.builder()
//                .title("모락 회의")
//                .anonymous(true)
//                .allowedCount(3)
//                .host(new Member(1L, "eden"))
//                .closed(false)
//                .closedAt(closedAt)
//                .build()
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 투표를_마감한다() {
//        // given
//        MorakDateTime closedAt = FakeDateTime.builder()
//                .dateTime(TIME_OF_2022_05_12_12_30)
//                .now(TIME_OF_2022_05_12_12_00)
//                .build();
//
//        Poll poll = Poll.builder()
//                .title("모락 회의")
//                .anonymous(true)
//                .allowedCount(3)
//                .host(엘리)
//                .closed(false)
//                .closedAt(closedAt)
//                .build();
//
//        // when
//        poll.close(엘리);
//
//        // then
//        assertThat(poll.getClosed()).isTrue();
//    }
//
//    @Test
//    void 호스트가_아닌_멤버가_마감시_예외를_던진다() {
//        // given
//        MorakDateTime closedAt = FakeDateTime.builder()
//                .dateTime(TIME_OF_2022_05_12_12_30)
//                .now(TIME_OF_2022_05_12_12_00)
//                .build();
//
//        Poll poll = Poll.builder()
//                .title("모락 회의")
//                .anonymous(true)
//                .allowedCount(3)
//                .host(엘리)
//                .closed(false)
//                .closedAt(closedAt)
//                .build();
//
//        // when & then
//        Assertions.assertThatThrownBy(() -> poll.close(에덴))
//                .isInstanceOf((IllegalArgumentException.class));
//    }
//
//    @Test
//    void 투표_마감_시_이미_마감된_투표면_예외를_던진다() {
//        // given
//        MorakDateTime closedAt = FakeDateTime.builder()
//                .dateTime(TIME_OF_2022_05_12_12_30)
//                .now(TIME_OF_2022_05_12_12_00)
//                .build();
//
//        Poll poll = Poll.builder()
//                .title("모락 회의")
//                .anonymous(true)
//                .allowedCount(3)
//                .host(엘리)
//                .closed(true)
//                .closedAt(closedAt)
//                .build();
//
//        // when & then
//        Assertions.assertThatThrownBy(() -> poll.close(엘리))
//                .isInstanceOf((IllegalArgumentException.class));
//    }
}
