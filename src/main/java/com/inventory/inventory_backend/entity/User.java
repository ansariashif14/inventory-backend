package com.inventory.inventory_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table
(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    (strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
}

