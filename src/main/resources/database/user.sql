CREATE TABLE users
(
    id       UUID         NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255),
    password VARCHAR(500) NOT NULL,
    PRIMARY KEY (id, username)
);

CREATE TABLE roles
(
    id   UUID         NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id, name)
);

CREATE TABLE users_roles
(
    user_username VARCHAR(255) NOT NULL,
    role_name     VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_username) REFERENCES users (username),
    FOREIGN KEY (role_name) REFERENCES roles (name),
    PRIMARY KEY (user_username, role_name)
)