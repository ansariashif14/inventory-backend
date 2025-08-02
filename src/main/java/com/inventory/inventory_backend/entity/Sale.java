package com.inventory.inventory_backend.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Generated;

import org.springframework.boot.autoconfigure.domain.EntityScan;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Sale {
    
	@Id 
	@GeneratedValue
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;
    private BigDecimal totalCost;
    private Instant timestamp;
    private Instant soldAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleCostDetail> costDetails = new ArrayList();
}

