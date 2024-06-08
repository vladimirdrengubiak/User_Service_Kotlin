package com.example.userapp;
import com.example.userapp.dto.UserDTO;
import com.example.userapp.exception.UserIdMismatchException;
import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.model.User;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "john", "password", "ROLE_USER");
        userDTO = new UserDTO(1L, "John", "john", "password", "ROLE_USER");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).name());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);
        assertEquals("John", result.name());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testCreateUser() {
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);
        assertEquals("John", result.name());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);
        assertEquals("John", result.name());
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode(userDTO.password());
    }

    @Test
    void testUpdateUser_UserIdMismatch() {
        assertThrows(UserIdMismatchException.class, () -> userService.updateUser(2L, userDTO));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
