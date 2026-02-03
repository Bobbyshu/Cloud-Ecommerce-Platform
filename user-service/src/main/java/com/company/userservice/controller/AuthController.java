package com.company.userservice.controller;

import com.company.userservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return authService.generateToken(username, password);
    }
}