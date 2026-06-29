package com.julia.paymentservice.services;

import com.julia.paymentservice.dto.OrderPayment;
import com.julia.paymentservice.entities.Payment;
import com.julia.paymentservice.entities.PaymentStatus;
import com.julia.paymentservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    public List<Payment> findAll(){
        return paymentRepository.findAll();
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id).get();
    }

    public Payment insert(Payment payment){
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment payment){
        Payment p = findById(id);
        p.setAmount(payment.getAmount());
        p.setStatus(payment.getStatus());
        return paymentRepository.save(p);
    }

    public void delete(Long id){
        paymentRepository.deleteById(id);
    }


    @KafkaListener(topics = "order-waiting-payment", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listenReserved(OrderPayment orderPayment) {
        if (paymentRepository.existsByOrderId(orderPayment.id())) {
            return;
        }
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(orderPayment.amount());
        payment.setOrderId(orderPayment.id());
        payment.setMethod(orderPayment.method());

        paymentRepository.save(payment);
        processPayment(payment, orderPayment.token());
    }

    public void processPayment(Payment payment, String token){
        boolean approved = fakeGateway(token);

        if(approved){
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
            kafkaTemplate.send("payment-approved", payment.getOrderId().toString(), payment.getOrderId());
        }else{
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            kafkaTemplate.send("payment-failed", payment.getOrderId().toString(), payment.getOrderId());
        }
    }

    private boolean fakeGateway(String token) {

        if (token == null || token.isBlank())
            return false;

        if(token.startsWith("tok_ok"))
            return true;

        if(token.startsWith("tok_fail"))
            return false;

        return Math.random() < 0.8;
    }

}
