INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('12345678', 'eden', 'http://eden-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('97533356', 'ellie', 'http://ellie-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('12345679', 'bkr', 'http://bkr-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('12345669', 'chalee', 'http://chalee-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('12345668', 'albur', 'http://albur-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at)
VALUES ('12345677', 'winnie', 'http://winnie-profile.com', now(), now());

INSERT INTO team (name, code, created_at, updated_at)
VALUES ('morak', 'MoraK123', now(), now());

INSERT INTO team (name, code, created_at, updated_at)
VALUES ('betrayed', 'Betrayed', now(), now());

INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (1, 1, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (1, 2, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (1, 3, now(), now());

INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (2, 4, now(), now());

INSERT INTO poll (team_code, host_id, title, allowed_count, anonymous, status, created_at, updated_at, closed_at,
                  code)
VALUES ('MoraK123', 2, 'test-poll-title', 2, true, 'OPEN', now(), now(), now(), 'testcode');

INSERT INTO appointment (team_code, host_id, title, sub_title, start_date, end_date, start_time, end_time,
                         duration_minutes, status, code, closed_at, created_at, updated_at)
VALUES ('MoraK123', 2, '발표 준비 날짜 정하기', '데모 데이 발표 준비를 위한..', '2122-08-01', '2122-08-04', '13:00:00', '22:00:00',
        60, 'OPEN', 'FEsd23C1', '2022-07-31T23:59:00', now(), now());

INSERT INTO slack_webhook (team_id, url, created_at, updated_at)
VALUES (1L, 'https://slack.webhook.com/', now(), now());
