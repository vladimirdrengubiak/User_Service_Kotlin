package com.example.userapp.service;

import com.example.userapp.dto.UserDTO;
import com.example.userapp.exception.UserIdMismatchException;
import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::fromUser)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.fromUser(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encode password before saving
        user.setId(null);
        return userMapper.fromUser(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        if (!id.equals(userDTO.id())) {
            throw new UserIdMismatchException("Mismatch between URL ID (" + id + ") and request body ID (" + userDTO.id() + ")");
        }
        User existingUser = findUserById(id);
        existingUser.setName(userDTO.name());
        existingUser.setUsername(userDTO.username());
        existingUser.setRole(userDTO.role());

        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        return userMapper.fromUser(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

}
