package com.julia.paymentservice.resources;

import com.julia.paymentservice.entities.Payment;
import com.julia.paymentservice.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payment Resource", description = "Endpoints for payment management")
@RestController
@RequestMapping("/payments")
public class PaymentResource {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Find all payments")
    @GetMapping
    public ResponseEntity<List<Payment>> findAll(){
        return ResponseEntity.ok().body(paymentService.findAll());
    }

    @Operation(summary = "Find payment by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(paymentService.findById(id));
    }

    @Operation(summary = "Insert a new payment")
    @PostMapping
    public ResponseEntity<Payment> insert(@RequestBody Payment obj){
        return ResponseEntity.ok().body(paymentService.insert(obj));
    }

    @Operation(summary = "Delete a payment by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a payment by id")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment obj){
        obj = paymentService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
