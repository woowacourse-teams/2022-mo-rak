package com.morak.back.notification.domain.slack;

import com.morak.back.core.domain.BaseEntity;
import com.morak.back.team.domain.Team;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@NoArgsConstructor
@Getter
public class SlackWebhook extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Team team;

    @NotBlank(message = "URL 은 공백일 수 없습니다.")
    @URL(regexp = "https://hooks.slack.com/services/.*", message = "웹훅 URL 은 http로 시작해야 합니다.")
    private String url;

    @Builder
    public SlackWebhook(final Long id, final Team team, final String url) {
        super(id);
        this.team = team;
        this.url = url;
    }
}
