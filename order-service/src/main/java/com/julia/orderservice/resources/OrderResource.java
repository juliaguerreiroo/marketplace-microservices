package com.julia.orderservice.resources;

import com.julia.orderservice.dto.OrderDto;
import com.julia.orderservice.entities.Order;
import com.julia.orderservice.entities.PaymentData;
import com.julia.orderservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Order Resource", description = "Endpoints for order management")
@RestController
@RequestMapping("/orders")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Find all orders")
    @GetMapping
    public ResponseEntity<List<Order>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @Operation(summary = "Find order by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @Operation(summary = "Insert a new order")
    @PostMapping
    public ResponseEntity<Order> insert(@RequestBody OrderDto orderDto){
        Order order  = orderService.insert(orderDto);
        URI uri  = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @Operation(summary = "Delete an order by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an order by id")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order){
        order = orderService.update(id, order);
        return ResponseEntity.ok().body(order);
    }


}
