package com.julia.orderservice.repositories;

import com.julia.orderservice.entities.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {
    Optional<PaymentData> findByOrderId(Long id);
}
