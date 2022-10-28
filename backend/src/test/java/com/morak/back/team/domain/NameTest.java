package com.morak.back.team.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.team.exception.TeamDomainLogicException;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    void 팀이름을_생성한다() {
        // given
        String value = "hello";
        // when & then
        Name name = new Name(value);
    }

    @Test
    void 팀이름의_길이는_공백일수_없다() {
        // given
        String value = "    ";
        // when & then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(TeamDomainLogicException.class);
    }

    @Test
    void 팀이름의_길이는_255자를_넘을_수_없다() {
        // given
        String value = "x".repeat(255 + 1);
        // when & then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(TeamDomainLogicException.class);
    }
}
