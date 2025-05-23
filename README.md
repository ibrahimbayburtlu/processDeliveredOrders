# Startup Heroes Order Processing System

This project is a Spring Boot application that processes delivered orders and sends them to Kafka.

## Requirements

- Java 17
- Maven
- Docker and Docker Compose
- MySQL 8.0

## Setup Steps

1. Clone the project:
```bash
git clone https://github.com/ibrahimbayburtlu/processDeliveredOrders.git
cd processDeliveredOrders
```

2. Create MySQL database:
```sql
CREATE DATABASE startupheroes;
CREATE USER 'startupheroes'@'localhost' IDENTIFIED BY 'startupheroes';
GRANT ALL PRIVILEGES ON startupheroes.* TO 'startupheroes'@'localhost';
FLUSH PRIVILEGES;
```

3. Start Kafka:
```bash
docker-compose up -d
```

4. Build and run the application:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

## API Usage

You can access the Postman collection [here](https://www.postman.co/workspace/My-Workspace~7dccbd6a-2662-4207-844c-851c8075a9f9/collection/43250738-f85e51d6-690d-4eb7-b08d-816e7be323c6?action=share&creator=43250738).

### 1. List All Orders
```bash
curl http://localhost:8081/orders
```

### 2. Get Order by ID
```bash
curl http://localhost:8081/orders/{id}
```

### 3. Process Delivered Orders for a Specific Date
```bash
curl --location 'http://localhost:8081/orders/process/2024-02-20'
```

## Monitoring Kafka Messages

To monitor messages sent to Kafka:

### Listen to all messages from the beginning
```bash
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic order_delivery_statistics --from-beginning
```

### Listen only to new messages
```bash
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic order_delivery_statistics
```
## Project Structure

```
src/main/java/com/startupheroes/startupheroes/
├── controller/     # REST API endpoints (OrderController)
├── service/        # Business logic implementation (OrderService, OrderServiceImpl)
├── repository/     # Database operations (OrderRepository)
├── entity/         # JPA entities (Order)
├── model/          # Data Transfer Objects (DeliveredOrder)
├── kafka/          # Kafka producers (OrderKafkaProducer)
├── config/         # Application configuration (KafkaConfig)
├── constant/       # Application constants (KafkaTopics,Endpoints)
└── exception/      # Custom exceptions (OrderNotFoundException, GlobalExceptionHandler)
```

## Testing

To run tests:
```bash
./mvnw test
```

## Technologies

- Spring Boot 3.4.5
- Spring Data JPA
- Spring Kafka
- MySQL
- Apache Kafka
- Docker

## Database Structure

The application uses MySQL database with the following structure:

### Orders Table
```sql
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
```

### Sample Data
The application comes with 25 sample orders that cover different scenarios across two days (February 20-21, 2024):

#### February 20, 2024 Orders

1. **Delivered Orders (6 orders)**
   - 3 orders delivered on time (order_in_time: true)
     - Order 6: ETA 50min, Actual 45min
     - Order 8: ETA 60min, Actual 55min
     - Order 10: ETA 45min, Actual 40min
   - 3 orders delivered late (order_in_time: false)
     - Order 1: ETA 30min, Actual 60min
     - Order 7: ETA 30min, Actual 60min
     - Order 9: ETA 40min, Actual 45min
     - Order 11: ETA 35min, Actual 50min

2. **Undelivered Orders (9 orders)**
   - Not yet picked (2 orders)
     - Order 2
     - Order 12
   - Collection started but not completed (2 orders)
     - Order 3
     - Order 13
   - Picked but not yet dispatched (2 orders)
     - Order 4
     - Order 14
   - Delivery started but not yet delivered (3 orders)
     - Order 5
     - Order 15

#### February 21, 2024 Orders

1. **Delivered Orders (7 orders)**
   - 4 orders delivered on time (order_in_time: true)
     - Order 16: ETA 40min, Actual 35min
     - Order 18: ETA 50min, Actual 45min
     - Order 20: ETA 45min, Actual 40min
     - Order 25: ETA 35min, Actual 30min
   - 3 orders delivered late (order_in_time: false)
     - Order 17: ETA 25min, Actual 40min
     - Order 19: ETA 30min, Actual 35min

2. **Undelivered Orders (3 orders)**
   - Not yet picked (1 order)
     - Order 21
   - Collection started but not completed (1 order)
     - Order 22
   - Picked but not yet dispatched (1 order)
     - Order 23
   - Delivery started but not yet delivered (1 order)
     - Order 24

The orders are distributed across February 20-21, 2024 to demonstrate different order states and delivery scenarios.
