package com.julia.authservice.dto;

import com.julia.authservice.entities.Role;

public record RegisterDto (String name, String email, String password, Role role) {
}
