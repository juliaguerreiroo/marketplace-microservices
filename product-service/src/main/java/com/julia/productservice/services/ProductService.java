package com.julia.productservice.services;

import com.julia.productservice.dto.*;
import com.julia.productservice.entities.Product;
import com.julia.productservice.exceptions.InsufficientStockException;
import com.julia.productservice.exceptions.InvalidDataException;
import com.julia.productservice.exceptions.ProductNotFoundException;
import com.julia.productservice.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
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
        if(id == null){
            throw new InvalidDataException("Id is required");
        }
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        return new ProductDto(obj.get().getId(), obj.get().getName(), obj.get().getPrice());
    }

    public Product internFindById(Long id){
        if(id == null){
            throw new InvalidDataException("Id is required");
        }
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        return obj.get();
    }

    public Product insert(Product product){
        if(product.getName() == null || product.getName().isBlank()){
            throw new InvalidDataException("Name is required");
        }

        if(product.getPrice() == null){
            throw new InvalidDataException("Price is required");
        }

        if(product.getStock() == null){
            throw new InvalidDataException("Stock is required");
        }

        if(product.getCategories().isEmpty()){
            throw new InvalidDataException("At least one category is required");
        }

        product.setName(product.getName().trim());
        return productRepository.save(product);
    }

    public void delete(Long id){
        findById(id);
        productRepository.deleteById(id);
    }

    public Product update(Long id, Product product){
        Product entity = internFindById(id);
        if(product.getName() == null || product.getName().isBlank()){
            throw new InvalidDataException("Name is required");
        }

        if(product.getDescription() == null || product.getDescription().isBlank()){
            throw new InvalidDataException("Description is required");
        }

        if(product.getPrice() == null){
            throw new InvalidDataException("Price is required");
        }

        if(product.getStock() == null){
            throw new InvalidDataException("Stock is required");
        }

        if(product.getCategories().isEmpty()){
            throw new InvalidDataException("At least one category is required");
        }

        entity.setName(product.getName().trim());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.getCategories().clear();
        entity.getCategories().addAll(product.getCategories());

        return productRepository.save(entity);
    }

    public void updateStock(Long id, Product product){
        Product p = internFindById(id);
        if(product.getStock() == null){
            throw new InvalidDataException("Stock is required");
        }
        Product entity = productRepository.getReferenceById(id);
        entity.setStock(product.getStock());
        productRepository.save(entity);
    }

    public void processStock(OrderEvent orderEvent){
        for (OrderItemDto item : orderEvent.items()) {
            Product product = internFindById(item.productId());
            if (item.quantity() > product.getStock()) {
                throw new InsufficientStockException("Insufficient stock");
            }
        }
    }

    @Transactional
    @KafkaListener(topics = "order-processed", containerFactory = "productKafkaListenerContainerFactory")
    public void listen(OrderEvent orderEvent) {
        try{
            processStock(orderEvent);
            for (OrderItemDto item : orderEvent.items()) {
                Product product = internFindById(item.productId());
                product.setStock(product.getStock() - item.quantity());
                updateStock(product.getId(), product);
            }
            kafkaTemplate.send("stock-reserved", orderEvent.id().toString(), orderEvent.id());
        }catch (InsufficientStockException e){
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

