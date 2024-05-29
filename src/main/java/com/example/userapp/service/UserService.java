package com.example.userapp.service;

import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
}
