```mermaid
graph TD
    A[Start] --> B[Extract Orders]
    B --> C{Data Validation}
    
    C -->|Valid| D[Transform Orders]
    C -->|Invalid| E[Error Handling]
    E --> F[Alert Team]
    F --> G[Retry]
    G --> B
    
    D --> H[Calculate Delivery Times]
    D --> I[Clean Data]
    
    H --> J[Load to Kafka]
    I --> J
    
    J --> K{Delivery Verification}
    K -->|Success| L[End]
    K -->|Failure| M[Retry Logic]
    M --> J
    
    subgraph "Error Handling"
        E
        F
        G
    end
    
    subgraph "Data Processing"
        H
        I
    end
    
    subgraph "Kafka Integration"
        J
        K
        M
    end
    
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style L fill:#9f9,stroke:#333,stroke-width:2px
    style C fill:#ff9,stroke:#333,stroke-width:2px
    style K fill:#ff9,stroke:#333,stroke-width:2px
```

## Flow Açıklaması

1. **Başlangıç (Start)**
   - DAG'ın başlangıç noktası
   - Zamanlanmış tetikleyici (her gece yarısı)

2. **Veri Çekme (Extract Orders)**
   - MySQL'den siparişleri çeker
   - Belirli tarih aralığındaki siparişleri filtreler

3. **Veri Doğrulama (Data Validation)**
   - Veri bütünlüğü kontrolü
   - Zorunlu alanların kontrolü
   - Format kontrolü

4. **Veri İşleme (Transform Orders)**
   - Teslimat sürelerinin hesaplanması
   - Veri temizleme ve formatlama
   - İş kurallarının uygulanması

5. **Kafka'ya Gönderme (Load to Kafka)**
   - İşlenmiş verilerin Kafka'ya gönderimi
   - Partition stratejisi
   - Batch işleme

6. **Gönderim Doğrulama (Delivery Verification)**
   - Kafka'ya gönderim başarısı kontrolü
   - ACK kontrolü
   - Hata durumu yönetimi

7. **Hata Yönetimi (Error Handling)**
   - Retry mekanizması
   - Alert sistemi
   - Loglama

8. **Bitiş (End)**
   - Başarılı tamamlanma
   - Metrik toplama
   - Raporlama 