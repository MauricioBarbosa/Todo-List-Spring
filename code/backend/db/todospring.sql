CREATE TABLE todospring_users (
  id VARCHAR(40) PRIMARY KEY NOT NULL UNIQUE,
  email VARCHAR(90) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  fullname VARCHAR(300) NOT NULL,
  password VARCHAR(10) NOT NULL,
  picture_path VARCHAR(200)
  confirmed BOOLEAN NOT NULL CHECK (confirmed IN (0, 1))
);

CREATE TABLE todospring_tasks (
  id VARCHAR(40) PRIMARY KEY NOT NULL UNIQUE,
  user_id VARCHAR(40) NOT NULL,
  is_deleted INTEGER NOT NULL CHECK (is_deleted IN (0, 1)),
  deadline VARCHAR(70),
  status VARCHAR(70),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE todospring_shared_tasks (
  id VARCHAR(40) PRIMARY KEY NOT NULL UNIQUE,
  owner_id VARCHAR(40) NOT NULL,
  receptor_id VARCHAR(40) NOT NULL,
  task_id VARCHAR(40) NOT NULL,
  FOREIGN KEY (task_id) REFERENCES Tasks(id),
  FOREIGN KEY (receptor_id) REFERENCES Users(id),
  FOREIGN KEY (owner_id) REFERENCES Users(id)
);
