-- Veritabanını oluştur
CREATE DATABASE IF NOT EXISTS startupheroes;
USE startupheroes;

-- Eğer tablo varsa sil
DROP TABLE IF EXISTS orders;

-- Tabloyu oluştur
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL,
    last_updated_at DATETIME NOT NULL,
    collection_started_at DATETIME,
    collected_at DATETIME,
    delivery_started_at DATETIME,
    delivery_at DATETIME,
    eta INT NOT NULL,
    customer_id BIGINT NOT NULL,
    INDEX idx_delivery_at (delivery_at),
    INDEX idx_customer_id (customer_id)
);

-- Örnek verileri ekle
INSERT INTO orders (created_at, last_updated_at, collection_started_at, collected_at, delivery_started_at, delivery_at, eta, customer_id) VALUES
-- Sipariş 1: Gecikmeli teslim edilmiş sipariş (ETA: 30 dk, Gerçek: 60 dk) - order_in_time: false
('2024-02-20 08:00:00', '2024-02-20 09:05:00', '2024-02-20 08:10:00', '2024-02-20 08:30:00', '2024-02-20 08:35:00', '2024-02-20 09:00:00', 30, 1),

-- Sipariş 2: Henüz toplanmamış sipariş
('2024-02-20 09:15:00', '2024-02-20 09:20:00', null, null, null, null, 45, 2),

-- Sipariş 3: Toplanmaya başlanmış ama henüz toplanmamış sipariş
('2024-02-20 10:30:00', '2024-02-20 10:35:00', '2024-02-20 10:40:00', null, null, null, 60, 3),

-- Sipariş 4: Toplanmış ama teslimata başlanmamış sipariş
('2024-02-20 11:00:00', '2024-02-20 11:15:00', '2024-02-20 11:05:00', '2024-02-20 11:10:00', null, null, 60, 4),

-- Sipariş 5: Teslimata başlanmış ama henüz teslim edilmemiş sipariş
('2024-02-20 12:00:00', '2024-02-20 12:15:00', '2024-02-20 12:05:00', '2024-02-20 12:10:00', '2024-02-20 12:12:00', null, 75, 5),

-- Sipariş 6: Zamanında teslim edilmiş sipariş (ETA: 50 dk, Gerçek: 45 dk) - order_in_time: true
('2024-02-20 13:00:00', '2024-02-20 13:45:00', '2024-02-20 13:10:00', '2024-02-20 13:20:00', '2024-02-20 13:25:00', '2024-02-20 13:45:00', 50, 6),

-- Sipariş 7: Gecikmeli teslim edilmiş sipariş (ETA: 30 dk, Gerçek: 60 dk) - order_in_time: false
('2024-02-20 14:00:00', '2024-02-20 15:00:00', '2024-02-20 14:10:00', '2024-02-20 14:20:00', '2024-02-20 14:25:00', '2024-02-20 15:00:00', 30, 7),

-- Sipariş 8: Zamanında teslim edilmiş sipariş (ETA: 60 dk, Gerçek: 55 dk) - order_in_time: true
('2024-02-20 15:00:00', '2024-02-20 15:55:00', '2024-02-20 15:10:00', '2024-02-20 15:20:00', '2024-02-20 15:25:00', '2024-02-20 15:55:00', 60, 8),

-- Sipariş 9: Gecikmeli teslim edilmiş sipariş (ETA: 40 dk, Gerçek: 45 dk) - order_in_time: false
('2024-02-20 16:00:00', '2024-02-20 16:45:00', '2024-02-20 16:05:00', '2024-02-20 16:10:00', '2024-02-20 16:15:00', '2024-02-20 16:45:00', 40, 9),

-- Sipariş 10: Zamanında teslim edilmiş sipariş (ETA: 45 dk, Gerçek: 40 dk) - order_in_time: true
('2024-02-20 17:00:00', '2024-02-20 17:40:00', '2024-02-20 17:05:00', '2024-02-20 17:15:00', '2024-02-20 17:20:00', '2024-02-20 17:40:00', 45, 10);

