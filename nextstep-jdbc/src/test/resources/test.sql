DROP TABLE IF EXISTS TEST_USERS;

CREATE TABLE TEST_USERS (
	userId          varchar(12)		NOT NULL,
	password		varchar(12)		NOT NULL,
	name			varchar(20)		NOT NULL,
	email			varchar(50),

	PRIMARY KEY               (userId)
);

INSERT INTO TEST_USERS VALUES('admin', 'password', '히브리', '히브리@woowa.com');
INSERT INTO TEST_USERS VALUES('test1', 'password', 'test', 'test1@woowa.com');
INSERT INTO TEST_USERS VALUES('test2', 'password', 'test', 'test2@woowa.com');