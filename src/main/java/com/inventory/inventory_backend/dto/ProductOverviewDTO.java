package com.inventory.inventory_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ProductOverviewDTO {
    private String productId;
    private int currentQuantity;
    private BigDecimal totalInventoryCost;
    private BigDecimal averageCostPerUnit;
}

