package com.company.userservice.service.impl;

import com.company.userservice.dao.UserRepository;
import com.company.userservice.entity.User;
import com.company.userservice.exception.UserNotExistException;
import com.company.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
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
}
