package com.julia.orderservice.services;

import com.julia.orderservice.dto.*;
import com.julia.orderservice.entities.*;
import com.julia.orderservice.exceptions.InvalidDataException;
import com.julia.orderservice.exceptions.OrderNotFoundException;
import com.julia.orderservice.exceptions.PaymentDataNotFoundException;
import com.julia.orderservice.feignclients.ProductFeignClient;
import com.julia.orderservice.repositories.OrderRepository;
import com.julia.orderservice.repositories.PaymentDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private PaymentDataRepository paymentDataRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id){
        if(id == null){
            throw new InvalidDataException("Id is required");
        }
        Optional<Order> obj = orderRepository.findById(id);
        if(obj.isEmpty()){
            throw new OrderNotFoundException("Order not found");
        }
        return obj.get();
    }

    public Order insert(OrderDto orderDto, PaymentData paymentData){

        if(orderDto.items().isEmpty()){
            throw new InvalidDataException("At least one item is required");
        }
        if(orderDto.userId() == null){
            throw new InvalidDataException("UserId is required");
        }
        if(paymentData.getCardToken() == null){
            throw new InvalidDataException("CardToken is required");
        }
        if(paymentData.getMethod() == null){
            throw new InvalidDataException("Method is required");
        }
        Order order = new Order();

        order.setUserId(orderDto.userId());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(java.time.LocalDateTime.now());

        for(OrderItemDto item : orderDto.items()){
            OrderItem orderItem = new OrderItem();

            ProductDto productDto = productFeignClient.findById(item.productId()).getBody();
            if(productDto == null){
                throw new InvalidDataException("One or more products are invalid");
            }
            Product product = new Product(productDto.id(), productDto.name(), productDto.price());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }
        order.calculateTotal();
        Order savedOrder = orderRepository.save(order);
        paymentData.setOrderId(savedOrder.getId());
        paymentDataRepository.save(paymentData);

        OrderEvent orderEvent = new OrderEvent(savedOrder.getId(), orderDto.items());
        kafkaTemplate.send("order-processed", orderEvent.id().toString(), orderEvent);

        return savedOrder;
    }

    public void delete(Long id){
        findById(id);
        orderRepository.deleteById(id);
    }

    public Order update(Long id, Order order){
        if(order.getStatus() == null){
            throw new InvalidDataException("Status is required");
        }
        if(order.getItems().isEmpty()){
            throw new InvalidDataException("At least one item is required");
        }
        Order entity = findById(id);
        entity.setStatus(order.getStatus());
        entity.getItems().clear();
        for (OrderItem item : order.getItems()) {
            item.setOrder(entity);
            entity.getItems().add(item);
        }
        entity.calculateTotal();
        return orderRepository.save(entity);
    }

    @KafkaListener(topics = "stock-rejected", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenRejected(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Transactional
    @KafkaListener(topics = "stock-reserved", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenReserved(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        orderRepository.save(order);

        PaymentData paymentData = paymentDataRepository.findByOrderId(id).orElseThrow(() -> new PaymentDataNotFoundException("Payment Data not found"));
        OrderPayment orderPayment = new OrderPayment(id, order.getTotal(), paymentData.getMethod(), paymentData.getCardToken());
        kafkaTemplate.send("order-waiting-payment", id.toString(), orderPayment);
        paymentDataRepository.delete(paymentData);
    }

    @KafkaListener(topics = "payment-approved", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenApproved(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "payment-failed", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenFailed(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        List<OrderItemDto> items = order.getItems()
                .stream()
                .map(item -> new OrderItemDto(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();

        OrderEvent event = new OrderEvent(order.getId(), items);

        kafkaTemplate.send("order-stock-rollback", order.getId().toString(), event);
    }
}
