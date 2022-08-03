package com.morak.back.core.domain.slack;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SlackWebhookRepository extends Repository<SlackWebhook, Long> {

    SlackWebhook save(SlackWebhook slackWebhook);

    Optional<SlackWebhook> findByTeamId(Long teamId);

    void deleteByTeamId(Long teamId);
}
