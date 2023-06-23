CREATE TABLE users
(
    id       VARCHAR(50) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255),
    password VARCHAR(500) NOT NULL
)