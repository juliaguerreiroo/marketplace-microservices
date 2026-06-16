package com.julia.orderservice.dto;

import java.util.ArrayList;
import java.util.List;

public record OrderDto(Long userId, List<OrderItemDto> items) {

}
