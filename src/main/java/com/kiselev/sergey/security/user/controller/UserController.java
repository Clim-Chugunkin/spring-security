package com.kiselev.sergey.security.user.controller;


import com.kiselev.sergey.security.user.DTO.AuthRequestDto;
import com.kiselev.sergey.security.user.DTO.UserDTO;
import com.kiselev.sergey.security.user.mapper.UserDTOMapper;
import com.kiselev.sergey.security.user.model.User;
import com.kiselev.sergey.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/user")
    public UserDTO addUser(@RequestBody AuthRequestDto request) {
        return UserDTOMapper.toUserDTO(userService.addNewUser(request.toUser(passwordEncoder)));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('write')")
    public UserDTO addAdmin(@RequestBody AuthRequestDto request) {
        return UserDTOMapper.toUserDTO(userService.addNewUser(request.toAdmin(passwordEncoder)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserDTOMapper::toUserDTO)
                .toList();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('read')")
    public UserDTO getUserById(@PathVariable(name = "userId") Long userId,
                               @AuthenticationPrincipal User user) {// get authenticated user
        System.out.println(user.getEmail());
        return UserDTOMapper.toUserDTO(userService.getUserById(userId));
    }

}
