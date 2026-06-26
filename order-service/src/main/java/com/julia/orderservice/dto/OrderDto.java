package com.julia.orderservice.dto;

import java.util.List;

public record OrderDto(Long userId, List<OrderItemDto> items) {

}
