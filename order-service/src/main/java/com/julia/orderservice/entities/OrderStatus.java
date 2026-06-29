package com.julia.orderservice.entities;

public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    WAITING_PAYMENT;
}