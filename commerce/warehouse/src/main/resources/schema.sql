CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.products (
    id VARCHAR(50) PRIMARY KEY,
    fragile BOOLEAN NOT NULL,
    width DECIMAL NOT NULL,
    height DECIMAL NOT NULL,
    depth DECIMAL NOT NULL,
    weight DECIMAL NOT NULL,
    quantity BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse.order_bookings (
    id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    delivery_id VARCHAR(50),
    delivery_weight DECIMAL NOT NULL,
    delivery_volume DECIMAL NOT NULL,
    fragile BOOLEAN NOT NULL,
);

CREATE TABLE IF NOT EXISTS warehouse.booked_products (
    booking_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (booking_id, product_id),
    FOREIGN KEY (booking_id)
             REFERENCES warehouse.order_bookings(id)
             ON DELETE CASCADE,
    FOREIGN KEY (product_id)
            REFERENCES warehouse.products(id)
            ON DELETE RESTRICT
);
