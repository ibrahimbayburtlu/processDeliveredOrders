-- Create database
CREATE DATABASE IF NOT EXISTS startupheroes;
USE startupheroes;

-- Drop table if exists
DROP TABLE IF EXISTS orders;

-- Create table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL,
    last_updated_at DATETIME NOT NULL,
    collection_started_at DATETIME,
    collected_at DATETIME,
    delivery_started_at DATETIME,
    delivery_at DATETIME,
    eta INT NOT NULL,
    customer_id BIGINT NOT NULL
);

-- Insert sample data
INSERT INTO orders (created_at, last_updated_at, collection_started_at, collected_at, delivery_started_at, delivery_at, eta, customer_id) VALUES
-- Order 1: Delivered late (ETA: 30 min, Actual: 60 min) - order_in_time: false
('2024-02-20 08:00:00', '2024-02-20 09:05:00', '2024-02-20 08:10:00', '2024-02-20 08:30:00', '2024-02-20 08:35:00', '2024-02-20 09:00:00', 30, 1),

-- Order 2: Not yet picked
('2024-02-20 09:15:00', '2024-02-20 09:20:00', null, null, null, null, 45, 2),

-- Order 3: Collection started but not completed
('2024-02-20 10:30:00', '2024-02-20 10:35:00', '2024-02-20 10:40:00', null, null, null, 60, 3),

-- Order 4: Picked but not yet dispatched
('2024-02-20 11:00:00', '2024-02-20 11:15:00', '2024-02-20 11:05:00', '2024-02-20 11:10:00', null, null, 60, 4),

-- Order 5: Delivery started but not yet delivered
('2024-02-20 12:00:00', '2024-02-20 12:15:00', '2024-02-20 12:05:00', '2024-02-20 12:10:00', '2024-02-20 12:12:00', null, 75, 5),

-- Order 6: Delivered on time (ETA: 50 min, Actual: 45 min) - order_in_time: true
('2024-02-20 13:00:00', '2024-02-20 13:45:00', '2024-02-20 13:10:00', '2024-02-20 13:20:00', '2024-02-20 13:25:00', '2024-02-20 13:45:00', 50, 6),

-- Order 7: Delivered late (ETA: 30 min, Actual: 60 min) - order_in_time: false
('2024-02-20 14:00:00', '2024-02-20 15:00:00', '2024-02-20 14:10:00', '2024-02-20 14:20:00', '2024-02-20 14:25:00', '2024-02-20 15:00:00', 30, 7),

-- Order 8: Delivered on time (ETA: 60 min, Actual: 55 min) - order_in_time: true
('2024-02-20 15:00:00', '2024-02-20 15:55:00', '2024-02-20 15:10:00', '2024-02-20 15:20:00', '2024-02-20 15:25:00', '2024-02-20 15:55:00', 60, 8),

-- Order 9: Delivered late (ETA: 40 min, Actual: 45 min) - order_in_time: false
('2024-02-20 16:00:00', '2024-02-20 16:45:00', '2024-02-20 16:05:00', '2024-02-20 16:10:00', '2024-02-20 16:15:00', '2024-02-20 16:45:00', 40, 9),

-- Order 10: Delivered on time (ETA: 45 min, Actual: 40 min) - order_in_time: true
('2024-02-20 17:00:00', '2024-02-20 17:40:00', '2024-02-20 17:05:00', '2024-02-20 17:15:00', '2024-02-20 17:20:00', '2024-02-20 17:40:00', 45, 10);

