CREATE TABLE tasks (
   id UUID PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   description TEXT,
   status VARCHAR(50) NOT NULL,
   priority VARCHAR(50) NOT NULL,
   due_date TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   user_id UUID,

   CONSTRAINT fk_tasks_user
       FOREIGN KEY (user_id)
           REFERENCES users(id)
);