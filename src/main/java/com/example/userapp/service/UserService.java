package com.example.userapp.service;

import com.example.userapp.dto.UserDTO;
import com.example.userapp.exception.UserIdMismatchException;
import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.exception.UsernameAlreadyExistsException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
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
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new UsernameAlreadyExistsException("Username " + userDTO.username() + " already exists");
        }
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
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new UsernameAlreadyExistsException("Username " + userDTO.username() + " already exists");
        }

        User existingUser = findUserById(id);

        Optional.ofNullable(userDTO.name()).
                filter(StringUtils::hasText).
                ifPresent(existingUser::setName);

        Optional.ofNullable(userDTO.username()).
                filter(StringUtils::hasText).
                ifPresent(existingUser::setUsername);

        Optional.ofNullable(userDTO.role()).
                filter(StringUtils::hasText).
                ifPresent(existingUser::setRole);

        Optional.ofNullable(userDTO.password())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(existingUser::setPassword);

        return userMapper.fromUser(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

}
