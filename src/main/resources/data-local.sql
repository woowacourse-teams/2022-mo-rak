INSERT INTO member (email, name, created_at, updated_at) values('test-mail-1@email.com', 'test-name1', now(), now());
INSERT INTO member (email, name, created_at, updated_at) values('test-mail-2@email.com', 'test-name2', now(), now());
INSERT INTO member (email, name, created_at, updated_at) values('test-mail-3@email.com', 'test-name3', now(), now());

INSERT INTO team (name, code, created_at, updated_at) values('team-name-1', 'test-code-1', now(), now());

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


