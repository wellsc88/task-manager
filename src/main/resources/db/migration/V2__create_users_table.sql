CREATE TABLE users (
   id UUID PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   role_id UUID NOT NULL,
   enabled BOOLEAN NOT NULL DEFAULT TRUE,

   CONSTRAINT fk_users_role
     FOREIGN KEY (role_id)
     REFERENCES roles(id)
);