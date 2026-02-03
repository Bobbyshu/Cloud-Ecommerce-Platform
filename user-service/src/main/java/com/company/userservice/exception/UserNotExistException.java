package com.company.userservice.exception;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(Long id){
        super("User with id " + id + " does not exist");
    }
}
