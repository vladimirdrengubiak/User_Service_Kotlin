package com.example.userapp;

import com.example.userapp.controller.UserController;
import com.example.userapp.dto.UserDTO;
import com.example.userapp.exception.GlobalExceptionHandler;
import com.example.userapp.exception.UserIdMismatchException;
import com.example.userapp.exception.UserNotFoundException;
import com.example.userapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UserDTO userDTO;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Add global exception handler
                .build();
        userDTO = new UserDTO(1L, "John", "john", "password", "ROLE_USER");
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}]"));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}"));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("User with id 1 not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":404,\"message\":\"User with id 1 not found\"}"));
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        String userJson = "{\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}"));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        String userJson = "{\"id\":1,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}";

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}"));
    }

    @Test
    void updateUser_UserIdMismatch() throws Exception {
        userDTO.setId(2L);
        String userJson = "{\"id\":2,\"name\":\"John\",\"username\":\"john\",\"password\":\"password\",\"role\":\"ROLE_USER\"}";

        doThrow(new UserIdMismatchException("Mismatch between url ID (1) and request body ID (2)"))
                .when(userService).updateUser(eq(1L), any(UserDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":400,\"message\":\"Mismatch between url ID (1) and request body ID (2)\"}"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new UserNotFoundException("User with id 1 not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":404,\"message\":\"User with id 1 not found\"}"));
    }
}
