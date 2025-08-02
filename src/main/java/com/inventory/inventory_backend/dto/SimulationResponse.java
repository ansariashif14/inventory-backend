package com.inventory.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimulationResponse {
    private int generatedCount;
    private List<TransactionDTO> transactions;
}
