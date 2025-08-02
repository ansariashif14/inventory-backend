package com.inventory.inventory_backend.dto;


import lombok.Data;

@Data
public class SaleRequest {
    private String productId;
    private int quantity;
}
