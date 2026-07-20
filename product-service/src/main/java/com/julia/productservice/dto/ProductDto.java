package com.julia.productservice.dto;

import java.io.Serializable;

public record ProductDto(Long id, String name, Double price) implements Serializable {
}
