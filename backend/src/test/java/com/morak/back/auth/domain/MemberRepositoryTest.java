package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 멤버를_저장한다() {
        // given
        Member member = new Member(null, "87654321", "ellie", "ellie-profile.com");

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMember).isNotNull(),
                () -> assertThat(savedMember.getId()).isNotNull(),
                () -> assertThat(savedMember.getName()).isEqualTo("ellie")
        );
    }

    @Test
    public void oauth_id로_멤버를_조회한다() {
        // given
        String oauthId = "12345678";

        // when
        Optional<Member> member = memberRepository.findByOauthId(oauthId);

        // then
        Assertions.assertAll(
                () -> assertThat(member).isPresent(),
                () -> assertThat(member.get().getName()).isEqualTo("eden")
        );
    }
}

