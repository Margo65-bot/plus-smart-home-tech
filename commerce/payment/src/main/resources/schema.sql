CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment.payments (
    id VARCHAR(50) PRIMARY KEY,
    state VARCHAR(20) NOT NULL,
    total_payment DECIMAL NOT NULL,
    delivery_total DECIMAL NOT NULL,
    fee_total DECIMAL NOT NULL,
    order_id VARCHAR(50) NOT NULL,
);