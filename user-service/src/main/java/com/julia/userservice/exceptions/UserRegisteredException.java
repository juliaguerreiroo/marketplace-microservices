package com.julia.userservice.exceptions;

public class UserRegisteredException extends RuntimeException {
    public UserRegisteredException(String message) {
        super(message);
    }
}
