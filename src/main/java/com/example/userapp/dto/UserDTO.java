package com.example.userapp.dto;

import com.example.userapp.validation.ValidationGroups;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotBlank;

public record UserDTO (
    Long id,
    @NotBlank(message = "Name is mandatory", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    String name,
    @NotBlank(message = "Username is mandatory", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "Username must be less than 50 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    String username,

    @NotBlank(message = "Password is mandatory", groups = ValidationGroups.Create.class)
    @Size(min = 5, message = "Password must be at least 5 characters long", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    String password,
    @NotBlank(message = "Role is mandatory", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN)$", message = "Role must be either ROLE_USER or ROLE_ADMIN", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    String role
    ) {

}