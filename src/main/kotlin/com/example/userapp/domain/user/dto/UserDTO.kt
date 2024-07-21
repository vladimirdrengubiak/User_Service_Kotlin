package com.example.userapp.domain.user.dto

import com.example.userapp.domain.user.validation.ValidationGroups
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserDTO(
    val id: Long?,
    @field:NotBlank(
        message = "Name is mandatory",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    @field:Pattern(
        regexp = "^[A-Za-z]+$",
        message = "Name must contain only letters",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    val name: String,
    @field:NotBlank(
        message = "Username is mandatory",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    @field:Size(
        max = 50,
        message = "Username must be less than 50 characters",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    val username: String,
    @field:NotBlank(
        message = "Password is mandatory",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    @field:Size(
        min = 5,
        message = "Password must be at least 5 characters long",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    val password: String,
    @field:NotBlank(
        message = "Role is mandatory",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    @field:Pattern(
        regexp = "^(ROLE_USER|ROLE_ADMIN)$",
        message = "Role must be either ROLE_USER or ROLE_ADMIN",
        groups = [ValidationGroups.Create::class, ValidationGroups.Update::class]
    )
    val role: String
)
