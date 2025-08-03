package com.inventory.inventory_backend.kafka;



import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.inventory.inventory_backend.dto.InventoryEvent;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;

@Component
public class InventoryEventProducer {
    
	
	private KafkaTemplate<String, InventoryEvent> kafkaTemplate;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String inventoryTopic;
	
	@Autowired
    public InventoryEventProducer(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendInventoryEvent(InventoryEvent event) {
    	
    	
        try {
            SendResult<String, InventoryEvent> result =
                kafkaTemplate.send(inventoryTopic, event.getProductId(), event).get(); // ⬅️ blocks here

            System.out.printf("✅ Sent to topic %s | Partition: %d | Offset: %d%n",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (Exception ex) {
            System.err.printf("❌ Failed to send event: %s%n", ex.getMessage());
        }
    }

}


