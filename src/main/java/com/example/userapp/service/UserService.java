package com.example.userapp.service;

import com.example.userapp.dto.UserDTO;
import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = findUserById(id);
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = findUserById(userDTO.getId());
        user.setName(userDTO.getName());
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

}
