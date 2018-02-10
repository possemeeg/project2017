CREATE TABLE localuser (
  username VARCHAR(256) NOT NULL,
  name VARCHAR(256) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE message (
  id BIGINT NOT NULL,
  message VARCHAR(256),
  sender VARCHAR(256),
  PRIMARY KEY (id)
);

CREATE TABLE broadcast (
  message_id BIGINT NOT NULL,
  PRIMARY KEY (message_id),
  FOREIGN KEY (message_id)
     REFERENCES message(id)
     ON DELETE CASCADE
);

CREATE TABLE localuser_message (
  message_id BIGINT NOT NULL,
  localuser_username VARCHAR(256) NOT NULL,
  FOREIGN KEY (message_id)
     REFERENCES message(id)
     ON DELETE CASCADE
);

