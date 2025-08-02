package com.inventory.inventory_backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SaleCostDetail {
    @Id
    @GeneratedValue 
    private Long id;

    @ManyToOne
    private Sale sale;

    @ManyToOne
    private InventoryBatch batch;

    private int quantity;
    private BigDecimal unitPrice;
}

