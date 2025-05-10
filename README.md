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
