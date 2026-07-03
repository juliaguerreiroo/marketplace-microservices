package com.julia.orderservice.exceptions;

public class PaymentDataNotFoundException extends RuntimeException {
    public PaymentDataNotFoundException(String message) {
        super(message);
    }
}
