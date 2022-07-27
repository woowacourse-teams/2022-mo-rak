package com.morak.back.auth.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Embeddable
@NoArgsConstructor
@Getter
public class ProfileUrl {

    @Size(min = 1, max = 255, message = "profileUrl의 길이는 1 ~ 255 사이여야합니다.")
    @URL(regexp = "^(http).*", message = "profileUrl은 http로 시작해야 합니다.")
    private String profileUrl;

    public ProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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
