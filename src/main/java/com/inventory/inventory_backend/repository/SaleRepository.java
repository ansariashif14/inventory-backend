package com.inventory.inventory_backend.repository;


import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_backend.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findBySoldAtAfter(Instant since);
}
