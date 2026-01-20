package com.kiselev.sergey.security.user.service;

import com.kiselev.sergey.security.user.model.User;

import java.util.List;

public interface UserService {
    User addNewUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);
}
