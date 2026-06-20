package com.julia.productservice.dto;

import com.julia.productservice.entities.Role;

public record FindByEmailDto(String email, String password, Role role) {
}
