package com.inventory.inventory_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {
    private String productId;
    private int quantity;
    private BigDecimal unitPrice;
}

