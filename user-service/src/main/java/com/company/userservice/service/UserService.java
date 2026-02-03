package com.company.userservice.service;

import com.company.userservice.entity.User;

public interface UserService {
    User createUser(User user);
    User findUserById(Long id);
    User updateUser(User user);
    void deleteUserById(Long id);
}
