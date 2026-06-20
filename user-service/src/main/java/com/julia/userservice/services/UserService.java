package com.julia.userservice.services;

import com.julia.userservice.dto.FindByEmailDto;
import com.julia.userservice.dto.RegisterDto;
import com.julia.userservice.entities.User;
import com.julia.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public FindByEmailDto findByEmail(String name){
        User user = userRepository.findByEmail(name);
        FindByEmailDto data = new FindByEmailDto(user.getEmail(), user.getPassword(), user.getRole());
        return data;
    }

    public User register(RegisterDto data){
        if(this.userRepository.findByEmail(data.email()) != null){
            return null;
        }
        String encryptedPassword = passwordEncoder.encode(data.password());
        User user= new User(data.name(), data.email(), encryptedPassword, data.role());

        this.userRepository.save(user);
        return user;
    }


}
