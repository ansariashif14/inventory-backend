package com.inventory.inventory_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_backend.entity.SaleCostDetail;

public interface SaleCostDetailRepository extends JpaRepository<SaleCostDetail, Long> {}
