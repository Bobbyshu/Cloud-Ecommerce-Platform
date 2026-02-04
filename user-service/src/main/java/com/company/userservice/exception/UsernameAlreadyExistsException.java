package com.company.userservice.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String name) {
        super("User with name " + name + " already exist");
    }
}
