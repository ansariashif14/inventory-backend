package com.inventory.inventory_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_backend.entity.InventoryBatch;
import com.inventory.inventory_backend.entity.Product;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {
    List<InventoryBatch> findByProductProductIdAndQuantityGreaterThanOrderByReceivedAtAsc(String productId, int qty);
}