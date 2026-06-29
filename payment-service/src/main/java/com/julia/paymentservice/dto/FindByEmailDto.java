package com.julia.paymentservice.dto;

import com.julia.paymentservice.entities.Role;

public record FindByEmailDto(String email, String password, Role role) {
}
