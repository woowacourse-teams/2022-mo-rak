INSERT INTO member (email, name, created_at, updated_at) values('test-mail-1@email.com', 'test-name1', now(), now());
INSERT INTO member (email, name, created_at, updated_at) values('test-mail-2@email.com', 'test-name2', now(), now());
INSERT INTO member (email, name, created_at, updated_at) values('test-mail-3@email.com', 'test-name3', now(), now());

INSERT INTO team (name, code, created_at, updated_at) values('team-name-1', 'test-code-1', now(), now());

INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 1, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 2, now(), now());
INSERT INTO team_member (team_id, member_id, created_at, updated_at) values(1, 3, now(), now());
