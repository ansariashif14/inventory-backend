package com.inventory.inventory_backend.entity;


import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
	@Id
    @GeneratedValue 
    private Long id;

    @Column(unique = true)
    private String productId;

    private String name;

    @OneToMany(mappedBy = "product")
    private List<InventoryBatch> batches = new ArrayList<>();

   
}
