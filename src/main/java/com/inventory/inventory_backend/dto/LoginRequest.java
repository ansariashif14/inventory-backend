package com.inventory.inventory_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
