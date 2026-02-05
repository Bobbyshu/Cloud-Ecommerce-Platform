package com.company.orderservice.exception;

public class UserIdInvalidException extends RuntimeException{
    public UserIdInvalidException(String message) {
        super(message);
    }
}
