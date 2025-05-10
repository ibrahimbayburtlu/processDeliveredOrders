# Startup Heroes Sipariş İşleme Sistemi

Bu proje, teslim edilmiş siparişleri işleyen ve Kafka'ya gönderen bir Spring Boot uygulamasıdır.

## Gereksinimler

- Java 17
- Maven
- Docker ve Docker Compose
- MySQL 8.0

## Kurulum Adımları

1. Projeyi klonlayın:
```bash
git clone https://github.com/ibrahimbayburtlu/processDeliveredOrders.git
cd processDeliveredOrders
```

2. MySQL veritabanını oluşturun:
```sql
CREATE DATABASE startupheroes;
CREATE USER 'startupheroes'@'localhost' IDENTIFIED BY 'startupheroes';
GRANT ALL PRIVILEGES ON startupheroes.* TO 'startupheroes'@'localhost';
FLUSH PRIVILEGES;
```

3. Kafka'yı başlatın:
```bash
docker-compose up -d
```

4. Uygulamayı derleyin ve çalıştırın:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

## API Kullanımı

### 1. Tüm Siparişleri Listele
```bash
curl http://localhost:8081/orders
```

### 2. ID'ye Göre Sipariş Getir
```bash
curl http://localhost:8081/orders/{id}
```

### 3. Belirli Bir Tarihteki Teslim Edilmiş Siparişleri İşle
```bash
curl --location 'http://localhost:8081/orders/process/2024-02-20'
```

## Kafka Mesajlarını İzleme

Kafka'ya gönderilen mesajları izlemek için:
```bash
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic order_delivery_statistics --from-beginning
```

## Proje Yapısı

```
src/main/java/com/startupheroes/startupheroes/
├── controller/     # API endpoint'leri
├── service/        # İş mantığı
├── repository/     # Veritabanı işlemleri
├── entity/         # Veritabanı modelleri
├── model/          # DTO'lar
├── kafka/          # Kafka işlemleri
├── config/         # Konfigürasyon sınıfları
└── exception/      # Özel exception'lar
```

## Test

Testleri çalıştırmak için:
```bash
./mvnw test
```

## Teknolojiler

- Spring Boot 3.4.5
- Spring Data JPA
- Spring Kafka
- MySQL
- Apache Kafka
- Docker 