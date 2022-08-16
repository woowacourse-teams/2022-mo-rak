package com.morak.back.core.domain.slack;

import com.morak.back.poll.domain.BaseEntity;
import com.morak.back.team.domain.Team;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SlackWebhook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Team team;

    @NotBlank
    @URL(regexp = "https://hooks.slack.com/services/.*", message = "웹훅 URL 은 http로 시작해야 합니다.")
    private String url;
}
