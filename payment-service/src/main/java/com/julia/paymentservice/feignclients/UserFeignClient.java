package com.julia.paymentservice.feignclients;

import com.julia.paymentservice.dto.FindByEmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/users")
public interface UserFeignClient {

    @GetMapping("/search")
    ResponseEntity<FindByEmailDto> findByEmail(@RequestParam("email") String email);
}
