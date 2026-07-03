package com.julia.paymentservice.services;

import com.julia.paymentservice.dto.OrderPayment;
import com.julia.paymentservice.entities.Payment;
import com.julia.paymentservice.entities.PaymentStatus;
import com.julia.paymentservice.exceptions.InvalidDataException;
import com.julia.paymentservice.exceptions.PaymentFailException;
import com.julia.paymentservice.exceptions.PaymentNotFoundException;
import com.julia.paymentservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        if(id == null){
            throw new InvalidDataException("Id is required");
        }
        Optional<Payment> obj = paymentRepository.findById(id);
        if(obj.isEmpty()){
            throw new PaymentNotFoundException("Payment not found");
        }
        return obj.get();
    }

    public Payment insert(Payment payment){
        if(payment.getStatus() == null){
            throw new InvalidDataException("Status is required");
        }
        if(payment.getAmount() == null){
            throw new InvalidDataException("Amount is required");
        }
        if(payment.getOrderId() == null){
            throw new InvalidDataException("OrderId is required");
        }
        if(payment.getMethod() == null){
            throw new InvalidDataException("Method is required");
        }
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment payment){
        Payment p = findById(id);
        if(payment.getAmount() == null){
            throw new InvalidDataException("Amount is required");
        }
        if(payment.getStatus() == null){
            throw new InvalidDataException("Status is required");
        }
        p.setAmount(payment.getAmount());
        p.setStatus(payment.getStatus());
        return paymentRepository.save(p);
    }

    public void delete(Long id){
        findById(id);
        paymentRepository.deleteById(id);
    }


    @KafkaListener(topics = "order-waiting-payment", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listenReserved(OrderPayment orderPayment) {
        findById(orderPayment.id());
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(orderPayment.amount());
        payment.setOrderId(orderPayment.id());
        payment.setMethod(orderPayment.method());

        insert(payment);
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
            throw new PaymentFailException("Payment failed");
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
