package com.julia.authservice.dto;

import com.julia.authservice.entities.Role;

public record FindByEmailDto(String email, String password, Role role) {
}
