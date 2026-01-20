package com.kiselev.sergey.security.user.mapper;

import com.kiselev.sergey.security.user.DTO.UserDTO;
import com.kiselev.sergey.security.user.model.User;

public class UserDTOMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
