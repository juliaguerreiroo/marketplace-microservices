package com.julia.orderservice.dto;

import com.julia.orderservice.entities.Role;

public record FindByEmailDto(String email, String password, Role role) {
}
