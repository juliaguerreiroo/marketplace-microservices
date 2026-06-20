package com.julia.orderservice.feignclients;

import com.julia.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "product-service", path = "/products")
public interface ProductFeignClient {
    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDto> findById(@PathVariable Long id);
}
