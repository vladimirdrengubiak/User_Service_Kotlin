package com.example.userapp.domain.user.controller

import com.example.userapp.domain.user.dto.UserDTO
import com.example.userapp.domain.user.service.UserService
import com.example.userapp.domain.user.validation.ValidationGroups
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations pertaining to user management")
@Validated
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "Get all users")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved list",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping
    fun getAllUsers(): List<UserDTO> = userService.getAllUsers()

    @Operation(summary = "Get user by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved user",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> =
        ResponseEntity.ok(userService.getUserById(id))

    @Operation(summary = "Create a new user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping
    fun createUser(@Validated(ValidationGroups.Create::class) @RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO))

    @Operation(summary = "Update an existing user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Invalid ID"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.userId")
    fun updateUser(
        @PathVariable("id") id: Long,
        @Validated(ValidationGroups.Update::class) @RequestBody userDTO: UserDTO
    ): ResponseEntity<UserDTO> =
        ResponseEntity.ok(userService.updateUser(id, userDTO))

    @Operation(summary = "Delete a user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User deleted"),
            ApiResponse(responseCode = "400", description = "Invalid ID"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.userId")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}
