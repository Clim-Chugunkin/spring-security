package com.kiselev.sergey.security.user.repository;

import com.kiselev.sergey.security.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
