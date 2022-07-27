package com.morak.back.auth.domain;

import com.morak.back.core.exception.InappropriateFormException;
import com.morak.back.core.exception.InappropriateLengthException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Embeddable
@NoArgsConstructor
@Getter
public class ProfileUrl {

    private String profileUrl;

    public ProfileUrl(String profileUrl) {
        validate(profileUrl);
        this.profileUrl = profileUrl;
    }

    private void validate(String profileUrl) {
        validateLength(profileUrl);
        validateForm(profileUrl);
    }

    private void validateForm(String profileUrl) {
        if (!profileUrl.startsWith("http")) {
            throw InappropriateFormException.of("profileUrl", profileUrl);
        }
    }

    private void validateLength(String profileUrl) {
        if (ObjectUtils.isEmpty(profileUrl)) {
            throw InappropriateLengthException.of("profileUrl", profileUrl);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfileUrl that = (ProfileUrl) o;
        return Objects.equals(profileUrl, that.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileUrl);
    }
}
