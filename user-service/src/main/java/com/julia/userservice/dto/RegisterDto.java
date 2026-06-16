package com.julia.userservice.dto;

import com.julia.userservice.entities.Role;

public record RegisterDto (String email, String password, Role role, String name) {
}
