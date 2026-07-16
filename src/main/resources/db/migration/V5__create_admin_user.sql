INSERT INTO users (
    id,
    name,
    email,
    password,
    role_id,
    enabled
)
VALUES (
   '839d2c44-433e-466d-9d5b-1a54f77755a5',
   'Administrator',
   'admin@taskmanager.com',
   '$2y$10$mkTIwWGbKuKKz49MGb1hj.LdCzdy4c3CMAEhwdyiODFhmUbLj//Ji',
   (SELECT id FROM roles WHERE name = 'ADMIN'),
   true
);