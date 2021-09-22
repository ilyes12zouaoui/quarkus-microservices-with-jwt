CREATE TABLE coffee
(
    id         INTEGER,
    name       VARCHAR(280) NOT NULL,
    price      DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP        NOT NULL,
    updated_at TIMESTAMP        NOT NULL,

    CONSTRAINT pk_coffee PRIMARY KEY (id)
);

CREATE SEQUENCE coffee_id_sequence START 1 INCREMENT 1 OWNED BY coffee.id;