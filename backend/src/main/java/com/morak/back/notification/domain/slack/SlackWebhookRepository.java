package com.morak.back.notification.domain.slack;

import com.morak.back.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SlackWebhookRepository extends Repository<SlackWebhook, Long> {

    SlackWebhook save(SlackWebhook slackWebhook);

    Optional<SlackWebhook> findByTeam(Team team);

    @Query("SELECT wh FROM SlackWebhook wh WHERE wh.team.code.code = :code")
    Optional<SlackWebhook> findByTeamCode(@Param("code") String code);

    void deleteByTeam(Team team);

    void flush();

    @Query("select wh from SlackWebhook wh where wh.team.code.code in :codes")
    List<SlackWebhook> findAllByTeams(@Param("codes") Iterable<String> codes);
}
