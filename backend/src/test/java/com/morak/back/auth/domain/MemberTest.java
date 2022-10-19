package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 멤버를_생성한다() {
        // given
        String name = "hello";
        String oauthId = "oauth-id";
        String profileUrl = "http://profile_url.com";

        // when & then
        assertThatNoException().isThrownBy(
                () -> Member.builder()
                        .name(name)
                        .oauthId(oauthId)
                        .profileUrl(profileUrl)
                        .build()
        );
    }

    @Test
    void 익명_멤버를_얻어낸다() {
        // given & when
        Member anonymous = Member.getAnonymousMember();

        // then
        assertThat(anonymous.getId()).isEqualTo(0L);
    }

    @Test
    void 멤버의_이름을_변경한다() {
        // given
        Member member = Member.builder()
                .name("RIANAEH")
                .oauthId("oauth-id")
                .profileUrl("http://profile_url.com")
                .build();

        // when
        member.changeName("엘리");

        // then
        assertThat(member.getName()).isEqualTo("엘리");
    }
}
