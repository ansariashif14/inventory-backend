package com.inventory.inventory_backend.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.inventory.inventory_backend.dto.InventoryEvent;
import com.inventory.inventory_backend.service.TransactionService;

@Component
public class InventoryEventListener {
  
	@Autowired 
  TransactionService service;

  @KafkaListener(topics="inventory-events", containerFactory="kafkaListenerContainerFactory")
  public void onEvent(InventoryEvent evt, Acknowledgment ack) {
    if ("purchase".equalsIgnoreCase(evt.getEventType())) {
    	service.processPurchase(evt);
    	ack.acknowledge();
    } else if ("sale".equalsIgnoreCase(evt.getEventType())) {
    	service.processSale(evt);
    	ack.acknowledge();
    }
  }
}


