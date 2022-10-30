DROP TABLE IF EXISTS role_match_result;
DROP TABLE IF EXISTS role_history;
DROP TABLE IF EXISTS role_name;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS select_member;
DROP TABLE IF EXISTS poll_item;
DROP TABLE IF EXISTS poll;
DROP TABLE IF EXISTS team_member;
DROP TABLE IF EXISTS team_invitation;
DROP TABLE IF EXISTS appointment_available_time;
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS slack_webhook;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `oauth_id`    VARCHAR(255) NOT NULL UNIQUE,
    `name`        VARCHAR(255) NOT NULL,
    `profile_url` VARCHAR(255) NOT NULL,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE team
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `code`       VARCHAR(255) NOT NULL UNIQUE,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE team_member
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `team_id`    BIGINT   NOT NULL,
    `member_id`  BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE team_invitation
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `team_id`    BIGINT       NOT NULL,
    `code`       VARCHAR(255) NOT NULL UNIQUE,
    `expired_at` DATETIME     NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id)
);

CREATE TABLE poll
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    allowed_count INT          NOT NULL,
    anonymous     BOOLEAN      NOT NULL,
    code          VARCHAR(255) NOT NULL,
    host_id       BIGINT       NOT NULL,
    status        VARCHAR(255) NOT NULL,
    team_code     VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    closed_at     DATETIME     NOT NULL,
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE poll_item
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    subject VARCHAR(255),
    poll_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

create table select_member
(
    poll_item_id BIGINT        NOT NULL,
    description  VARCHAR(1000) NOT NULL,
    member_id    BIGINT        NOT NULL,
    PRIMARY KEY (poll_item_id, member_id)
);

CREATE INDEX `index_poll` ON `poll` (`closed_at`);

CREATE TABLE appointment
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `team_code`        VARCHAR(255) NOT NULL,
    `host_id`          BIGINT       NOT NULL,
    `title`            VARCHAR(255) NOT NULL,
    `sub_title`        VARCHAR(255) NOT NULL,
    `start_date`       DATE         NOT NULL,
    `end_date`         DATE         NOT NULL,
    `start_time`       TIME         NOT NULL,
    `end_time`         TIME         NOT NULL,
    `duration_minutes` INTEGER      NOT NULL,
    `status`           VARCHAR(255) NOT NULL,
    `code`             VARCHAR(255) NOT NULL UNIQUE,
    `closed_at`        DATETIME     NOT NULL,
    `created_at`       DATETIME     NOT NULL,
    `updated_at`       DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_code) REFERENCES team (code),
    FOREIGN KEY (host_id) REFERENCES member (id)
);

CREATE INDEX `index_appointment` ON `appointment` (`closed_at`);

CREATE TABLE appointment_available_time
(
    `appointment_id`  BIGINT   NOT NULL,
    `member_id`       BIGINT   NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `created_at`      DATETIME NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

ALTER TABLE appointment_available_time
    ADD UNIQUE (appointment_id, member_id, start_date_time);

CREATE TABLE slack_webhook
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `team_id`    BIGINT       NOT NULL UNIQUE,
    `url`        VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id)
);

CREATE TABLE role
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `team_code`  VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE role_match_result
(
    `role_history_id` BIGINT       NOT NULL,
    `member_id`       BIGINT       NOT NULL,
    `role_name`       VARCHAR(255) NOT NULL,
    PRIMARY KEY (`role_history_id`, `member_id`, `role_name`)
);

CREATE TABLE role_name
(
    `role_id` BIGINT       NOT NULL,
    `name`    VARCHAR(255) NOT NULL
);

CREATE TABLE role_history
(
    `id`        BIGINT   NOT NULL AUTO_INCREMENT,
    `date_time` DATETIME NOT NULL,
    `role_id`   BIGINT   NOT NULL,
    PRIMARY KEY (`id`)
);
