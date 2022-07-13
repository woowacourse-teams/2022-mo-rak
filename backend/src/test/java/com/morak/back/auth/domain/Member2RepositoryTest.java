package com.morak.back.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.morak.back.support.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class Member2RepositoryTest {

    @Autowired
    private Member2Repository member2Repository;

    @Test
    public void oauth_id로_멤버를_조회한다() {
        // given
        Long oauthId = 45311765L;

        // when
        Optional<Member2> member = member2Repository.findByOauthId(oauthId);

        // then
        Assertions.assertAll(
                () -> assertThat(member).isPresent(),
                () -> assertThat(member.get().getName()).isEqualTo("RIANAEH")
        );
    }
}

