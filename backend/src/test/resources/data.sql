INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('12345678', 'eden', 'eden-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('97533356', 'ellie', 'ellie-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('12345679', 'bkr', 'bkr-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('12345669', 'chalee', 'chalee-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('12345668', 'albur', 'albur-profile.com', now(), now());
INSERT INTO member (oauth_id, name, profile_url, created_at, updated_at) values('12345677', 'winnie', 'winnie-profile.com', now(), now());

INSERT INTO team (name, code, created_at, updated_at) values('morak', 'MoraK123', now(), now());

INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 1, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 2, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 3, now(), now());

INSERT INTO poll (team_id, host_id, title, allowed_poll_count, is_anonymous, status, created_at, updated_at, closed_at,
                  code)
values (1, 1, 'test-poll-title', 2, true, 'OPEN', now(), now(), now(), 'test-code');

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
values (1, 'test-poll-item-subject-A', now(), now());

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
values (1, 'test-poll-item-subject-B', now(), now());

INSERT INTO poll_item (poll_id, subject, created_at, updated_at)
values (1, 'test-poll-item-subject-C', now(), now());

INSERT INTO poll_result (poll_item_id, member_id, created_at, updated_at)
values (1, 1, now(), now());

INSERT INTO poll_result (poll_item_id, member_id, created_at, updated_at)
values (2, 1, now(), now());

INSERT INTO poll_result (poll_item_id, member_id, created_at, updated_at)
values (2, 2, now(), now());

INSERT INTO poll_result (poll_item_id, member_id, created_at, updated_at)
values (3, 2, now(), now());
