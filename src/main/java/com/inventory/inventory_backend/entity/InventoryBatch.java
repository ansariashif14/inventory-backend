package com.inventory.inventory_backend.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InventoryBatch {
    
	@Id 
	@GeneratedValue 
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;
    private BigDecimal unitPrice;
    private Instant receivedAt;
}
