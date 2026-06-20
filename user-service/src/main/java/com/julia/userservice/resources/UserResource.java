package com.julia.userservice.resources;

import com.julia.userservice.dto.FindByEmailDto;
import com.julia.userservice.dto.RegisterDto;
import com.julia.userservice.entities.User;
import com.julia.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<FindByEmailDto> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.findByEmail(email));
    }

    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        User user = userService.register(registerDto);
        if(user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(user);
    }

}
