package com.company.userservice.service;

import com.company.userservice.dao.UserRepository;
import com.company.userservice.entity.User;
import com.company.userservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String generateToken(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        // send Token
        String level = user.getMembershipLevel() != null ? user.getMembershipLevel().name() : "SILVER";
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name(), level);
    }
}