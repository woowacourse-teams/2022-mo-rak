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

INSERT INTO team_member (team_id, member_id, created_at, updated_at)VALUES (1, 1, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (1, 2, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at)
VALUES (1, 3, now(), now());

INSERT INTO poll (team_id, host_id, title, allowed_poll_count, is_anonymous, status, created_at, updated_at, closed_at,
                  code)
VALUES (1, 1, 'test-poll-title', 2, true, 'OPEN', now(), now(), now(), 'testcode');

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
VALUES (1, 'test-poll-item-subject-A', now(), now());

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
VALUES (1, 'test-poll-item-subject-B', now(), now());

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
VALUES (1, 'test-poll-item-subject-C', now(), now());

INSERT INTO poll_result (poll_item_id, member_id, description, created_at, updated_at)
VALUES (1, 1, '까라는 매일 자니깐..', now(), now());

INSERT INTO poll_result (poll_item_id, member_id, description, created_at, updated_at)
VALUES (2, 1, '그냥뇨', now(), now());

INSERT INTO poll_result (poll_item_id, member_id, description, created_at, updated_at)
VALUES (2, 2, '', now(), now());

INSERT INTO poll_result (poll_item_id, member_id, description, created_at, updated_at)
VALUES (3, 2, '에덴이 시켰어요', now(), now());

INSERT INTO appointment (team_id, host_id, title, description, start_date, end_date, start_time, end_time,
                         duration_minutes, status, code, closed_at, created_at, updated_at)
VALUES (1, 2, '발표 준비 날짜 정하기', '데모 데이 발표 준비를 위한..', '2122-08-01', '2122-08-04', '13:00:00', '22:00:00', 60, 'OPEN',
        'FEsd23C1', '2022-07-31T23:59:00', now(), now());

INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, end_date_time, created_at,
                                        updated_at)
VALUES (1, 2, '2122-08-03T16:00:00', '2122-08-03T17:00:00', now(), now());

INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, end_date_time, created_at,
                                        updated_at)
VALUES (1, 2, '2122-08-02T16:00:00', '2122-08-02T17:00:00', now(), now());

INSERT INTO appointment_available_time (appointment_id, member_id, start_date_time, end_date_time, created_at,
                                        updated_at)
VALUES (1, 1, '2122-08-03T13:00:00', '2122-08-03T17:00:00', now(), now());
