package com.inventory.inventory_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryEvent {
    private String productId;
    private String eventType; 
    private int quantity;
    private BigDecimal unitPrice; 
    private Instant timestamp;
   
}

