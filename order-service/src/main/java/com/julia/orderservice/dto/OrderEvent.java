package com.julia.orderservice.dto;

import java.util.List;

public record OrderEvent(Long id, List<OrderItemDto> items) {
}
