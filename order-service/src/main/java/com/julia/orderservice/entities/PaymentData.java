package com.julia.orderservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_payment_data")
public class PaymentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String method;
    private String cardToken;

    public PaymentData(Long id, String method, String cardToken) {
        this.id = id;
        this.method = method;
        this.cardToken = cardToken;
    }
}
