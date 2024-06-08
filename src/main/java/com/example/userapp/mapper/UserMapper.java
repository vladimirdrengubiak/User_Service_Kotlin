package com.example.userapp.mapper;

import com.example.userapp.dto.UserDTO;
import com.example.userapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return User.builder()
                .id(userDTO.id())
                .name(userDTO.name())
                .username(userDTO.username())
                .password(userDTO.password())
                .role(userDTO.role())
                .build();
    }

    public UserDTO fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
