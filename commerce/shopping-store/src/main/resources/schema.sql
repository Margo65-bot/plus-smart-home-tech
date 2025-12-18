CREATE SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS shopping_store.products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_src VARCHAR(255),
    quantity_state VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL,
    category VARCHAR(20),
    price DECIMAL NOT NULL
);