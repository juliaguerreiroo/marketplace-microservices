package com.julia.userservice.services;

import com.julia.userservice.dto.FindByEmailDto;
import com.julia.userservice.dto.RegisterDto;
import com.julia.userservice.entities.User;
import com.julia.userservice.exceptions.EmailNotFoundException;
import com.julia.userservice.exceptions.InvalidUserDataException;
import com.julia.userservice.exceptions.UserRegisteredException;
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
        if (name == null || name.isBlank()) throw new InvalidUserDataException("E-mail is required");

        User user = userRepository.findByEmail(name);
        if(user == null){
            throw new EmailNotFoundException("E-mail not found");
        }

        return new FindByEmailDto(user.getEmail(), user.getPassword(), user.getRole());
    }

    public User register(RegisterDto data){
        if (data == null) {
            throw new InvalidUserDataException("Registration data is required");
        }

        if (data.name() == null || data.name().isBlank()) {
            throw new InvalidUserDataException("Name is required");
        }

        if (data.email() == null || data.email().isBlank()) {
            throw new InvalidUserDataException("E-mail is required");
        }

        if (data.password() == null || data.password().isBlank()) {
            throw new InvalidUserDataException("Password is required");
        }

        if (data.role() == null) {
            throw new InvalidUserDataException("Role is required");
        }

        if(this.userRepository.findByEmail(data.email()) != null){
            throw new UserRegisteredException("User is already registered");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User user= new User(data.name(), data.email(), encryptedPassword, data.role());

        this.userRepository.save(user);
        return user;
    }
}