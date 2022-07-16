package com.morak.back.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.exception.AuthorizationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    public void 토큰에서_memberId를_가져온다() {
        // given
        given(jwtTokenProvider.getPayload(anyString())).willReturn("1");

        // when
        Long memberId = authService.getMemberId("test-token");

        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    public void 존재하지않는_memberId인_경우_예외를_던진다() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.validateMemberId(1L))
                .isInstanceOf(AuthorizationException.class);
    }
}
