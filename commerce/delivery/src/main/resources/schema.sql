CREATE SCHEMA IF NOT EXISTS delivery;

CREATE TABLE IF NOT EXISTS delivery.deliveries (
    id VARCHAR(50) PRIMARY KEY,
    state VARCHAR(25) NOT NULL,
    total_weight DECIMAL NOT NULL,
    total_volume DECIMAL NOT NULL,
    fragile BOOLEAN NOT NULL,
    from_address VARCHAR(500) NOT NULL,
    to_address VARCHAR(500) NOT NULL,
    order_id VARCHAR(50) NOT NULL
);