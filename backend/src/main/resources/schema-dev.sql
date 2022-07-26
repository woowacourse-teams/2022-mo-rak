CREATE TABLE member
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `oauth_id`    varchar(255) NOT NULL UNIQUE,
    `name`        varchar(255) NOT NULL,
    `profile_url` varchar(255),
    `created_at`  datetime     NOT NULL,
    `updated_at`  datetime     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE team
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `name`       varchar(255) NOT NULL,
    `code`       varchar(255) NOT NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE team_member
(
    `id`         bigint   NOT NULL AUTO_INCREMENT,
    `team_id`    bigint   NOT NULL,
    `member_id`  bigint   NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE team_invitation
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `team_id`    bigint       NOT NULL,
    `code`       varchar(255) NOT NULL,
    `expired_at` datetime     NOT NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id)
);

CREATE TABLE `poll`
(
    `id`                 bigint       NOT NULL AUTO_INCREMENT,
    `team_id`            bigint       NOT NULL,
    `host_id`            bigint       NOT NULL,
    `title`              varchar(255) NOT NULL,
    `allowed_poll_count` int          NOT NULL,
    `is_anonymous`       boolean      NOT NULL,
    `status`             varchar(255) NOT NULL,
    `created_at`         datetime     NOT NULL,
    `updated_at`         datetime     NOT NULL,
    `closed_at`          datetime     NOT NULL,
    `code`               varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (host_id) REFERENCES member (id)
);

CREATE TABLE `poll_item`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `poll_id`    bigint       NOT NULL,
    `subject`    varchar(255) NOT NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (poll_id) REFERENCES poll (id)
);

CREATE TABLE poll_result
(
    `id`           bigint        NOT NULL AUTO_INCREMENT,
    `poll_item_id` bigint        NOT NULL,
    `member_id`    bigint        NOT NULL,
    `description`  varchar(1000) NOT NULL,
    `created_at`   datetime      NOT NULL,
    `updated_at`   datetime      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (poll_item_id) REFERENCES poll_item (id)
);

CREATE TABLE appointment
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `team_id`          BIGINT       NOT NULL,
    `host_id`          BIGINT       NOT NULL,
    `title`            VARCHAR(255) NOT NULL,
    `description`      VARCHAR(255) NOT NULL,
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
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (host_id) REFERENCES member (id)
);

CREATE TABLE appointment_available_time
(
    `id`              BIGINT   NOT NULL AUTO_INCREMENT,
    `appointment_id`  BIGINT   NOT NULL,
    `member_id`       BIGINT   NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `end_date_time`   DATETIME NOT NULL,
    `created_at`      DATETIME NOT NULL,
    `updated_at`      DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
