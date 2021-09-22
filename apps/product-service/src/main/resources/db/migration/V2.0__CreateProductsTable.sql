CREATE TABLE products
(
    id         INTEGER,
    user_id    INTEGER,
    name       VARCHAR(280),
    type       VARCHAR(150)     NOT NULL,
    price      DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP        NOT NULL,
    updated_at TIMESTAMP        NOT NULL,


    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_products FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE SEQUENCE products_id_sequence START 1 INCREMENT 1 OWNED BY products.id;