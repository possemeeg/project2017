CREATE TABLE localuser (
  username VARCHAR(256) NOT NULL,
  name VARCHAR(256) NOT NULL,
  PRIMARY KEY(username)
);

CREATE TABLE message (
  id BIGINT NOT NULL,
  message VARCHAR(256),
  PRIMARY KEY(id)
);

CREATE TABLE localuser_message (
  message_id BIGINT NOT NULL,
  localuser_username VARCHAR(256) NOT NULL
);

