package com.julia.orderservice.dto;


public record OrderPayment(Long id, Double amount, String method, String token) {
}
