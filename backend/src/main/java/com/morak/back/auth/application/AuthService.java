package com.morak.back.auth.application;

import com.morak.back.auth.domain.Member2Repository;
import com.morak.back.auth.exception.AuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final Member2Repository member2Repository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(Member2Repository member2Repository,
                       JwtTokenProvider jwtTokenProvider) {
        this.member2Repository = member2Repository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long getMemberId(String token) {
        return Long.parseLong(jwtTokenProvider.getPayload(token));
    }

    public void validateMemberId(Long memberId) {
        if (!member2Repository.existsById(memberId)) {
            throw new AuthorizationException("존재하지않는 멤버입니다.");
        }
    }
}
