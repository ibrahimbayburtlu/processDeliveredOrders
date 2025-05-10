# Startup Heroes Order Processing System

Bu proje, teslim edilmiş siparişleri işleyen ve Kafka'ya gönderen bir Spring Boot uygulamasıdır.

## Gereksinimler

- Java 17
- Maven
- Docker ve Docker Compose
- MySQL 8.0

## Kurulum

1. Projeyi klonlayın:
```bash
git clone [repo-url]
cd startupheroes
```

2. MySQL veritabanını oluşturun:
```sql
CREATE DATABASE startupheroes;
CREATE USER 'startupheroes'@'localhost' IDENTIFIED BY 'startupheroes123';
GRANT ALL PRIVILEGES ON startupheroes.* TO 'startupheroes'@'localhost';
FLUSH PRIVILEGES;
```

3. Kafka'yı başlatın:
```bash
docker-compose up -d
```

4. Uygulamayı başlatın:
```bash
./mvnw spring-boot:run
```

## API Kullanımı

### Teslim Edilmiş Siparişleri İşleme

```bash
curl -X POST http://localhost:8081/api/orders/process-delivered-orders?date=2024-05-09T10:00:00
```

## Proje Yapısı

- `src/main/java/com/startupheroes/startupheroes/controller`: API endpoint'leri
- `src/main/java/com/startupheroes/startupheroes/service`: İş mantığı
- `src/main/java/com/startupheroes/startupheroes/repository`: Veritabanı işlemleri
- `src/main/java/com/startupheroes/startupheroes/model`: Veri modelleri
- `src/main/java/com/startupheroes/startupheroes/kafka`: Kafka işlemleri

## Teknolojiler

- Spring Boot 3.4.5
- Spring Data JPA
- Spring Kafka
- MySQL
- Apache Kafka
- Docker 