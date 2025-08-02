package com.inventory.inventory_backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inventory.inventory_backend.dto.ProductOverviewDTO;
import com.inventory.inventory_backend.entity.InventoryBatch;
import com.inventory.inventory_backend.entity.Product;
import com.inventory.inventory_backend.repository.InventoryBatchRepository;
import com.inventory.inventory_backend.repository.ProductRepository;
import com.inventory.inventory_backend.service.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired TransactionService service;

    @GetMapping
    public List<ProductOverviewDTO> getProducts() {
        return service.loadProducts();
    }
}
