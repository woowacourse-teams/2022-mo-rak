package com.morak.back.auth.domain;


import com.morak.back.core.exception.CustomErrorCode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class ProfileUrl {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;
    private static final Pattern PROFILE_URL_PATTERN = Pattern.compile("^(http).*");

    @Column(name = "profile_url", nullable = false)
    private String value;

    public ProfileUrl(String value) {
        validateBlank(value);
        validateLength(value);
        validatePattern(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new MemberDomainLogicException(CustomErrorCode.PROFILE_URL_BLANK_ERROR,
                    "멤버 Profile Url은 `" + value + "`일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        int length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new MemberDomainLogicException(
                    CustomErrorCode.PROFILE_URL_LENGTH_ERROR,
                    "멤버 프로필 " + value + "는 길이가" + MIN_LENGTH + "이상, " + MAX_LENGTH + "이하여야 합니다.");
        }
    }

    private void validatePattern(String value) {
        Matcher matcher = PROFILE_URL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new MemberDomainLogicException(CustomErrorCode.PROFILE_URL_PATTERN_ERROR,
                    "멤버 프로필 " + value + "는 http로 시작하지 않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        ProfileUrl that = (ProfileUrl) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
