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
```bash
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic order_delivery_statistics --from-beginning
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
The application comes with 10 sample orders that cover different scenarios:

1. **Delivered Orders (6 orders)**
   - 3 orders delivered on time (order_in_time: true)
     - Order 6: ETA 50min, Actual 45min
     - Order 8: ETA 60min, Actual 55min
     - Order 10: ETA 45min, Actual 40min
   - 3 orders delivered late (order_in_time: false)
     - Order 1: ETA 30min, Actual 60min
     - Order 7: ETA 30min, Actual 60min
     - Order 9: ETA 40min, Actual 45min

2. **Undelivered Orders (4 orders)**
   - Order 2: Not yet picked
   - Order 3: Collection started but not completed
   - Order 4: Picked but not yet dispatched
   - Order 5: Delivery started but not yet delivered

All orders are dated within the same day (February 20, 2024) to demonstrate different order states and delivery scenarios.
