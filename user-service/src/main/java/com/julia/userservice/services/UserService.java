package com.julia.userservice.services;

import com.julia.userservice.entities.User;
import com.julia.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByEmail(String name){
        return userRepository.findByEmail(name);
    }

}
