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