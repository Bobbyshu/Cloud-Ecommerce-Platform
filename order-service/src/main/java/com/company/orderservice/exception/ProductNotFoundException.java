package com.company.orderservice.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product with ID: " + productId + " not exist");
    }
}