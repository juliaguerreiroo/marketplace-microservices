package com.julia.userservice.resources;

import com.julia.userservice.dto.FindByEmailDto;
import com.julia.userservice.dto.RegisterDto;
import com.julia.userservice.entities.User;
import com.julia.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Resource", description = "Endpoints for user management")
@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @Operation(summary = "Find user by email")
    @GetMapping("/search")
    public ResponseEntity<FindByEmailDto> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.findByEmail(email));
    }

    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        User user = userService.register(registerDto);
        if(user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(user);
    }

}
