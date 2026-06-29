package com.julia.paymentservice.repositories;

import com.julia.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    boolean existsByOrderId(Long id);
}
