CREATE SCHEMA IF NOT EXISTS shopping_cart;

CREATE TABLE IF NOT EXISTS shopping_cart.shopping_carts (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS shopping_cart.cart_products (
    shopping_cart_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (shopping_cart_id, product_id),
    FOREIGN KEY (shopping_cart_id)
        REFERENCES shopping_cart.shopping_carts(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_carts_username ON shopping_cart.shopping_carts(username);
CREATE INDEX IF NOT EXISTS idx_carts_is_active ON shopping_cart.shopping_carts(is_active);