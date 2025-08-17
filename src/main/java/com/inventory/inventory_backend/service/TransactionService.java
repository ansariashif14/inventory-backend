package com.inventory.inventory_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory_backend.dto.InventoryEvent;
import com.inventory.inventory_backend.dto.ProductOverviewDTO;
import com.inventory.inventory_backend.dto.TransactionDTO;
import com.inventory.inventory_backend.entity.InventoryBatch;
import com.inventory.inventory_backend.entity.Product;
import com.inventory.inventory_backend.entity.Sale;
import com.inventory.inventory_backend.entity.SaleCostDetail;
import com.inventory.inventory_backend.repository.InventoryBatchRepository;
import com.inventory.inventory_backend.repository.ProductRepository;
import com.inventory.inventory_backend.repository.SaleRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private InventoryBatchRepository batchRepo;

    @Autowired
    private SaleRepository saleRepo;

    @Transactional
    public TransactionDTO processPurchase(InventoryEvent evt) {
        Product product = productRepo.findByProductId(evt.getProductId())
                .orElseGet(() -> {
                    Product p = new Product();
                    p.setProductId(evt.getProductId());
                    p.setName(evt.getProductId());
                    return productRepo.save(p);
                });

        InventoryBatch batch = new InventoryBatch();
        batch.setProduct(product);
        batch.setQuantity(evt.getQuantity());
        batch.setUnitPrice(evt.getUnitPrice());
        batch.setReceivedAt(evt.getTimestamp() != null ? evt.getTimestamp() : Instant.now());

        batchRepo.save(batch);

        return new TransactionDTO("purchase", evt.getProductId(), evt.getQuantity(), evt.getUnitPrice(), batch.getReceivedAt());
    }

    @Transactional
    public TransactionDTO processSale(InventoryEvent evt) {
        Product product = productRepo.findByProductId(evt.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + evt.getProductId()));

        List<InventoryBatch> batches = batchRepo.findByProductProductIdAndQuantityGreaterThanOrderByReceivedAtAsc(
                evt.getProductId(), 0);

        int remaining = evt.getQuantity();
        BigDecimal totalCost = BigDecimal.ZERO;
        List<SaleCostDetail> details = new ArrayList<>();

        for (InventoryBatch batch : batches) {
            if (remaining <= 0) break;

            int used = Math.min(remaining, batch.getQuantity());
            BigDecimal cost = batch.getUnitPrice().multiply(BigDecimal.valueOf(used));

            batch.setQuantity(batch.getQuantity() - used);
            batchRepo.save(batch);

            SaleCostDetail detail = new SaleCostDetail();
            detail.setBatch(batch);
            detail.setQuantity(used);
            detail.setUnitPrice(batch.getUnitPrice());

            details.add(detail);
            totalCost = totalCost.add(cost);
            remaining -= used;
        }

        if (remaining > 0) {
            throw new RuntimeException("Not enough inventory to process sale for " + product.getProductId());
        }

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(evt.getQuantity());
        sale.setSoldAt(evt.getTimestamp() != null ? evt.getTimestamp() : Instant.now());
        sale.setTotalCost(totalCost);
        sale.setCostDetails(details);
        details.forEach(d -> d.setSale(sale));

        saleRepo.save(sale);

        return new TransactionDTO("sale", product.getProductId(), evt.getQuantity(), totalCost, sale.getSoldAt());
    }

    public List<ProductOverviewDTO> loadProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductOverviewDTO> dtos = new ArrayList<>();

        for (Product product : products) {
            List<InventoryBatch> batches = batchRepo.findByProductProductIdAndQuantityGreaterThanOrderByReceivedAtAsc(product.getProductId(), 0);

            int totalQuantity = batches.stream().mapToInt(InventoryBatch::getQuantity).sum();
            BigDecimal totalCost = batches.stream()
                    .map(b -> b.getUnitPrice().multiply(BigDecimal.valueOf(b.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal avgCost = totalQuantity > 0 ? totalCost.divide(BigDecimal.valueOf(totalQuantity), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

            dtos.add(new ProductOverviewDTO(product.getProductId(), totalQuantity, totalCost, avgCost));
        }

        return dtos;
    }

    public List<TransactionDTO> loadLedger() {
        List<TransactionDTO> result = new ArrayList<>();

        List<InventoryBatch> purchases = batchRepo.findAll();
        for (InventoryBatch b : purchases) {
            result.add(new TransactionDTO("purchase", b.getProduct().getProductId(), b.getQuantity(), b.getUnitPrice(), b.getReceivedAt()));
        }

        List<Sale> sales = saleRepo.findBySoldAtAfter(Instant.now());
        for (Sale s : sales) {
            result.add(new TransactionDTO("sale", s.getProduct().getProductId(), s.getQuantity(), s.getTotalCost(), s.getSoldAt()));
        }

        result.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return result;
    }
}
