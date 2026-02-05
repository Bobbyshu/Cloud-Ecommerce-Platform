package com.company.orderservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
}