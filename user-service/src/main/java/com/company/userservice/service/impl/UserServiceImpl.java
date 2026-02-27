package com.company.userservice.service.impl;

import com.company.userservice.client.NotificationClient;
import com.company.userservice.dao.UserRepository;
import com.company.userservice.dto.EmailNotificationRequest;
import com.company.userservice.entity.User;
import com.company.userservice.enums.Membership;
import com.company.userservice.exception.UserEmailAlreadyExistsException;
import com.company.userservice.exception.UsernameAlreadyExistsException;
import com.company.userservice.exception.UserNotExistException;
import com.company.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClient notificationClient;

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }

        if (user.getMembershipLevel() == null) {
            user.setMembershipLevel(Membership.SILVER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        sendWelcomeNotification(savedUser);
        return savedUser;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));
    }

    @Override
    public User updateUser(User user) {
        User existingUser = findUserById(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        // only update when fe update new role or level
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        if (user.getMembershipLevel() != null) {
            existingUser.setMembershipLevel(user.getMembershipLevel());
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private void sendWelcomeNotification(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = "Welcome to Cloud E-commerce Platform";
        String body = "Hi " + user.getUsername() + ", your account has been created successfully.";

        try {
            notificationClient.sendEmail(new EmailNotificationRequest(user.getEmail(), subject, body));
        } catch (Exception exception) {
            log.warn("User {} created, but welcome email failed: {}", user.getId(), exception.getMessage());
        }
    }
}
