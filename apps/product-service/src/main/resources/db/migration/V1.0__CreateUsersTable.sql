CREATE TABLE users
(
    id         INTEGER,
    firstName  VARCHAR(150) NOT NULL,
    lastName   VARCHAR(150) NOT NULL,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(200) NOT NULL,
    age        INTEGER      NOT NULL,
    role       VARCHAR(150) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE SEQUENCE users_id_sequence START 1 INCREMENT 1 OWNED BY users.id;