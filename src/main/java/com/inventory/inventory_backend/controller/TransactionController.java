package com.inventory.inventory_backend.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inventory.inventory_backend.dto.InventoryEvent;
import com.inventory.inventory_backend.dto.PurchaseRequest;
import com.inventory.inventory_backend.dto.SaleRequest;
import com.inventory.inventory_backend.dto.SimulateRequest;
import com.inventory.inventory_backend.dto.SimulationResponse;
import com.inventory.inventory_backend.dto.TransactionDTO;
import com.inventory.inventory_backend.entity.InventoryBatch;
import com.inventory.inventory_backend.entity.Sale;
import com.inventory.inventory_backend.entity.SaleCostDetail;
import com.inventory.inventory_backend.kafka.InventoryEventProducer;
import com.inventory.inventory_backend.repository.InventoryBatchRepository;
import com.inventory.inventory_backend.repository.ProductRepository;
import com.inventory.inventory_backend.repository.SaleRepository;
import com.inventory.inventory_backend.service.TransactionService;

import java.util.*;
import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired 
    TransactionService service;
	
	@Autowired
    private InventoryEventProducer producer;
	
    @PostMapping("/simulate")
    public SimulationResponse simulate(@RequestBody SimulateRequest req) {
    	List<TransactionDTO> results = new ArrayList<>();
        String[] productIds = {"PRD001", "PRD002", "PRD003"};
        String[] eventTypes = {"purchase", "sale"};      
        for (int i = 0; i < req.getCount(); i++) {
            String productId = productIds[i % productIds.length];
            String eventType = eventTypes[(int) (Math.random() * eventTypes.length)];
            int quantity = (int) (Math.random() * 10) + 1;
            
            if (i < productIds.length) {
            	   eventType = "purchase"; // Ensure first few are purchases
            	}

            InventoryEvent event = new InventoryEvent();
            event.setProductId(productId);
            event.setEventType(eventType);
            event.setQuantity(quantity);
            event.setTimestamp(Instant.now());

            if ("purchase".equals(eventType)) {
                BigDecimal unitPrice = BigDecimal.valueOf((Math.random() * 50) + 10).setScale(2, BigDecimal.ROUND_HALF_UP);
                event.setUnitPrice(unitPrice);
            }
            
            producer.sendInventoryEvent(event); // send to Kafka (real-time ingestion)
            TransactionDTO result;
            // Also process immediately for dashboard consistency
            try {
            	   result = "purchase".equals(eventType)
            	            ? service.processPurchase(event)
            	            : service.processSale(event);
            	} catch (RuntimeException e) {
            	   System.err.println("⚠️ Skipping event due to: " + e.getMessage());
            	   continue; // or add to a failed list
            	}

            results.add(result);
        }

        return new SimulationResponse(results.size(), results);
    }

    @PostMapping("/purchase")
    public TransactionDTO purchase(@RequestBody PurchaseRequest req) {
    	InventoryEvent event = new InventoryEvent();
        event.setProductId(req.getProductId());
        event.setEventType("purchase");
        event.setQuantity(req.getQuantity());
        event.setUnitPrice(req.getUnitPrice());
        event.setTimestamp(Instant.now());

        producer.sendInventoryEvent(event); // optional for async
        return service.processPurchase(event);
    }

    @PostMapping("/sale")
    public TransactionDTO sale(@RequestBody SaleRequest req) {
    	InventoryEvent event = new InventoryEvent();
        event.setProductId(req.getProductId());
        event.setEventType("sale");
        event.setQuantity(req.getQuantity());
        event.setTimestamp(Instant.now());

        producer.sendInventoryEvent(event); // optional for async
        return service.processSale(event);
    }
}

