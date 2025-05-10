package com.startupheroes.startupheroes.model;

import lombok.Data;
import lombok.Builder;
@Data
@Builder
public class DeliveredOrder {
    private Long id;

    private String created_at;

    private String last_updated_at;

    private int collection_duration;

    private int delivery_duration;

    private int eta;

    private int lead_time;

    private Boolean order_in_time;
    
}
