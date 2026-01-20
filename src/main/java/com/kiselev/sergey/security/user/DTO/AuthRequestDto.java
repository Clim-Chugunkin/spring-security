package com.kiselev.sergey.security.user.DTO;

import com.kiselev.sergey.security.user.model.Role;
import com.kiselev.sergey.security.user.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class AuthRequestDto {
    private String email;
    private String name;
    private String password;


    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    public User toAdmin(PasswordEncoder passwordEncoder) {
        User user = toUser(passwordEncoder);
        user.setRole(Role.ADMIN);
        return user;
    }
}
