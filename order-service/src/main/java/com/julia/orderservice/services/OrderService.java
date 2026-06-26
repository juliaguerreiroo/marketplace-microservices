package com.julia.orderservice.services;

import com.julia.orderservice.dto.OrderDto;
import com.julia.orderservice.dto.OrderEvent;
import com.julia.orderservice.dto.OrderItemDto;
import com.julia.orderservice.dto.ProductDto;
import com.julia.orderservice.entities.Order;
import com.julia.orderservice.entities.OrderItem;
import com.julia.orderservice.entities.OrderStatus;
import com.julia.orderservice.entities.Product;
import com.julia.orderservice.feignclients.ProductFeignClient;
import com.julia.orderservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id){
        Optional<Order> obj = orderRepository.findById(id);
        return obj.get();
    }

    public Order insert(OrderDto orderDto){

        Order order = new Order();

        order.setUserId(orderDto.userId());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(java.time.LocalDateTime.now());

        for(OrderItemDto item : orderDto.items()){
            OrderItem orderItem = new OrderItem();

            ProductDto productDto = productFeignClient.findById(item.productId()).getBody();
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

        OrderEvent orderEvent = new OrderEvent(savedOrder.getId(), orderDto.items());
        kafkaTemplate.send("order-processed", orderEvent.id().toString(), orderEvent);

        return savedOrder;
    }

    public void delete(Long id){
        orderRepository.deleteById(id);
    }

    public Order update(Long id, Order order){
        Order entity = orderRepository.getReferenceById(id);
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
    public void listen(OrderEvent orderEvent) {
        Order order = findById(orderEvent.id());
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
