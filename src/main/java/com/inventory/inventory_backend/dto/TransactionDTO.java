package com.inventory.inventory_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {
    private String type;          
    private String productId;
    private int quantity;
    private BigDecimal totalCost; 
    private Instant timestamp;
}

