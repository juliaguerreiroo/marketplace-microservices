package com.julia.productservice.services;

import com.julia.productservice.dto.*;
import com.julia.productservice.entities.Product;
import com.julia.productservice.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public ProductDto findById(Long id){
        Optional<Product> obj = productRepository.findById(id);
        return new ProductDto(obj.get().getId(), obj.get().getName(), obj.get().getPrice());
    }

    public Product internFindById(Long id){
        Optional<Product> obj = productRepository.findById(id);
        return obj.get();
    }

    public Product insert(Product product){
        return productRepository.save(product);
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }

    public Product update(Long id, Product product){
        Product entity = productRepository.getReferenceById(id);
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.getCategories().clear();
        entity.getCategories().addAll(product.getCategories());
        return productRepository.save(entity);
    }

    public void updateStock(Long id, Product product){
        Product entity = productRepository.getReferenceById(id);
        entity.setStock(product.getStock());
        productRepository.save(entity);
    }

    @Transactional
    @KafkaListener(topics = "order-processed", containerFactory = "productKafkaListenerContainerFactory")
    public void listen(OrderEvent orderEvent) {
        boolean allAvailable = true;

        for (OrderItemDto item : orderEvent.items()) {
            Product product = internFindById(item.productId());
            if (product == null || item.quantity() > product.getStock()) {
                allAvailable = false;
                break;
            }
        }
        if (allAvailable) {
            for (OrderItemDto item : orderEvent.items()) {
                Product product = internFindById(item.productId());
                product.setStock(product.getStock() - item.quantity());
                updateStock(product.getId(), product);
            }
            kafkaTemplate.send("stock-reserved", orderEvent.id().toString(), orderEvent.id());
        } else {
            kafkaTemplate.send("stock-rejected", orderEvent.id().toString(), orderEvent.id());
        }
    }

    @KafkaListener(topics = "order-stock-rollback", containerFactory = "productKafkaListenerContainerFactory")
    public void listenRollback(OrderEvent orderEvent) {
        for (OrderItemDto item : orderEvent.items()) {
            Product product = internFindById(item.productId());
            product.setStock(product.getStock() + item.quantity());
            updateStock(product.getId(), product);
        }
    }
}

