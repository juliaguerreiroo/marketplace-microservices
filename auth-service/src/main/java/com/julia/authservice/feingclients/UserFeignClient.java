package com.julia.authservice.feingclients;

import com.julia.authservice.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/users")
public interface UserFeignClient {

    @GetMapping("/search")
    ResponseEntity<User> findByEmail(@RequestParam("email") String email);
}
