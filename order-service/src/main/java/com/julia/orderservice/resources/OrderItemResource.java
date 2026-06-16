package com.julia.orderservice.resources;

import com.julia.orderservice.entities.OrderItem;
import com.julia.orderservice.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemResource {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping
    public ResponseEntity<List<OrderItem>> findAll(){
        return ResponseEntity.ok(orderItemRepository.findAll());
    }


}
