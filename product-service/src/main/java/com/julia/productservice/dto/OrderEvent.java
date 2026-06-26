package com.julia.productservice.dto;

import java.util.List;

public record OrderEvent(Long id, List<OrderItemDto> items) {
}
