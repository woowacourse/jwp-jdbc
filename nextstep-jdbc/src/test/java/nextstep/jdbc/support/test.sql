DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS ( 
	userId          varchar(12)		NOT NULL, 
	password		varchar(12)		NOT NULL,
	name			varchar(20)		NOT NULL,
	age			int,
	PRIMARY KEY               (userId)
);

INSERT INTO USERS VALUES('admin', 'password', '자바지기', 12);