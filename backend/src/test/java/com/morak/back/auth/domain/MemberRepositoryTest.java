package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.support.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

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

    @Test
    void 이미_저장된_회원을_가져온다() {
        Member savedMember = memberRepository.save(new Member(null, "12345", "test-name", "http://test-url.com"));

        Optional<Member> findMember = memberRepository.findByOauthId(savedMember.getOauthId());

        assertThat(findMember).isPresent();
    }

    @Test
    void OAUTH_ID가_같은_회원을_두번_저장할수_없다() {
        Member savedMember = memberRepository.save(new Member(null, "12345", "test-name", "http://test-url.com"));

        assertThatThrownBy(() -> memberRepository.save(new Member(null, "12345", "other-name", "http://other-url.com")))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}

