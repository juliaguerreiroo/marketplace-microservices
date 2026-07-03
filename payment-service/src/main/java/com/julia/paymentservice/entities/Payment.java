package com.julia.paymentservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus  status;
    private LocalDateTime createdAt;
    private String method;

    public Payment(Long orderId, Double amount, PaymentStatus status, String method) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.method = method;
    }
}
