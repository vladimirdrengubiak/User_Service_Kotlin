package com.example.userapp.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String name;
    private String username;
    private String password;

    private String role;
}