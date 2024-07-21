package com.example.userapp.domain.user.service

import com.example.userapp.domain.user.dto.UserDTO
import com.example.userapp.domain.user.exception.UserNotFoundException
import com.example.userapp.domain.user.exception.UsernameAlreadyExistsException
import com.example.userapp.domain.user.mapper.UserMapper
import com.example.userapp.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper
) {
    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDTO> =
        userRepository.findAll().map(userMapper::fromUser)

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserDTO =
        userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
            .let(userMapper::fromUser)

    @Transactional
    fun createUser(userDTO: UserDTO): UserDTO {
        if (userRepository.existsByUsername(userDTO.username)) {
            throw UsernameAlreadyExistsException("Username ${userDTO.username} already exists")
        }
        return userMapper.toUser(userDTO).apply {
            password = passwordEncoder.encode(password)
        }.let(userRepository::save).let(userMapper::fromUser)
    }

    @Transactional
    fun updateUser(id: Long, userDTO: UserDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }

        require(id == userDTO.id) { "Mismatch between URL ID ($id) and request body ID (${userDTO.id})" }

        return user.apply {
            name = userDTO.name
            username = userDTO.username
            password = passwordEncoder.encode(userDTO.password)
            role = userDTO.role
        }.let(userMapper::fromUser)
    }

    @Transactional
    fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User with id $id not found")
        }
        userRepository.deleteById(id)
    }
}
