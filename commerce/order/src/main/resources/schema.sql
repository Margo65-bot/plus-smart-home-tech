CREATE SCHEMA IF NOT EXISTS order;

CREATE TABLE IF NOT EXISTS order.orders (
    id VARCHAR(50) PRIMARY KEY,
    shopping_cart_id VARCHAR(50) NOT NULL,
    delivery_id VARCHAR(50) NOT NULL,
    payment_id VARCHAR(50) NOT NULL,
    state VARCHAR(30) NOT NULL,
    delivery_volume DECIMAL,
    delivery_weight DECIMAL,
    fragile BOOLEAN,
    product_price DECIMAL,
    delivery_price DECIMAL,
    total_price DECIMAL,
    username VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS order.order_products (
    order_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id)
         REFERENCES order.orders(id)
         ON DELETE CASCADE
);