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


    private static final Member MEMBER = Member.builder()
            .oauthId("87654321")
            .name("ellie")
            .profileUrl("http://ellie-profile.com")
            .build();

    @Test
    void 멤버를_저장한다() {
        // given & when
        Member savedMember = memberRepository.save(MEMBER);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMember).isNotNull(),
                () -> assertThat(savedMember.getId()).isNotNull(),
                () -> assertThat(savedMember)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "updatedAt", "createdAt")
                        .isEqualTo(MEMBER)
        );
    }

    @Test
    void 저장한_멤버를_ID로_조회한다() {
        // given
        Member savedMember = memberRepository.save(MEMBER);

        // when
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        Assertions.assertAll(
                () -> assertThat(findMember).isPresent(),
                () -> assertThat(findMember.get()).isEqualTo(savedMember)
        );
    }

    @Test
    public void oauth_id로_멤버를_조회한다() {
        // given
        Member savedMember = memberRepository.save(MEMBER);

        // when
        Optional<Member> findMember = memberRepository.findByOauthId(MEMBER.getOauthId());

        // then
        Assertions.assertAll(
                () -> assertThat(findMember).isPresent(),
                () -> assertThat(findMember.get())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(savedMember)
        );
    }

    @Test
    void OAUTH_ID가_같은_멤버가_이미_존재하면_저장할수_없다() {
        // given
        memberRepository.save(MEMBER);

        Member otherMember = Member.builder()
                .oauthId(MEMBER.getOauthId())
                .name(MEMBER.getName())
                .profileUrl(MEMBER.getProfileUrl())
                .build();

        // when & then
        assertThatThrownBy(() -> memberRepository.save(otherMember))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

