package com.julia.orderservice.dto;

import com.julia.orderservice.entities.PaymentData;

import java.util.List;

public record OrderDto(Long userId, List<OrderItemDto> items, PaymentData paymentData) {

}
