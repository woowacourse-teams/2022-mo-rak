package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.support.RepositoryTest;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
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
                () -> assertThat(savedMember.getName()).isEqualTo("ellie")
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
        Member savedMember = memberRepository.save(MEMBER);

        Optional<Member> findMember = memberRepository.findByOauthId(savedMember.getOauthId());

        assertThat(findMember).isPresent();
    }

    @Test
    void OAUTH_ID가_같은_회원을_두번_저장할수_없다() {
        Member otherMember = Member.builder()
                .oauthId(MEMBER.getOauthId())
                .name(MEMBER.getName())
                .profileUrl(MEMBER.getProfileUrl())
                .build();
        Member savedMember = memberRepository.save(MEMBER);

        assertThatThrownBy(() -> memberRepository.save(otherMember))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    void 길이가_0인_이름은_저장할_수_없다() {
        Member member = Member.builder()
                .oauthId("1234")
                .name("")
                .profileUrl("http://")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 길이가_255이상인_이름은_저장할_수_없다() {
        String name = "1".repeat(256);
        Member member = Member.builder()
                .oauthId("1234")
                .name(name)
                .profileUrl("http://")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 길이가_0인_oauthId는_저장할_수_없다() {
        Member member = Member.builder()
                .oauthId("")
                .name("name")
                .profileUrl("http://")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 길이가_255이상인_oauthId는_저장할_수_없다() {
        String oauthId = "1".repeat(256);
        Member member = Member.builder()
                .oauthId(oauthId)
                .name("name")
                .profileUrl("http://")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 길이가_0인_profileUrl는_저장할_수_없다() {
        Member member = Member.builder()
                .oauthId("1234")
                .name("name")
                .profileUrl("")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 길이가_255이상인_profileUrl는_저장할_수_없다() {

        String profileUrl = "http://" + "1".repeat(255);
        Member member = Member.builder()
                .oauthId("1234")
                .name("name")
                .profileUrl(profileUrl)
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 형식이_안맞는_profileUrl는_저장할_수_없다() {
        Member member = Member.builder()
                .oauthId("1234")
                .name("name")
                .profileUrl("htt")
                .build();
        assertThatThrownBy(() -> memberRepository.save(member))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

