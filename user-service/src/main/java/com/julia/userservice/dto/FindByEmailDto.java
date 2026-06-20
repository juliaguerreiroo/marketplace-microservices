package com.julia.userservice.dto;

import com.julia.userservice.entities.Role;

public record FindByEmailDto(String email, String password, Role role) {
}
