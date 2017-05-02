CREATE TABLE IF NOT EXISTS messages (
  id int AUTO_INCREMENT PRIMARY KEY,
  userId VARCHAR(255),
  message VARCHAR(4096),
  msg_date TIMESTAMP,
  reply VARCHAR(4096),
  reply_date TIMESTAMP
);

CREATE INDEX IF NOT EXISTS user_index ON messages(userId);