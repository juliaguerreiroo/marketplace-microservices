package com.julia.paymentservice.dto;

public record OrderPayment(Long id, Double amount, String method, String token) {
}
