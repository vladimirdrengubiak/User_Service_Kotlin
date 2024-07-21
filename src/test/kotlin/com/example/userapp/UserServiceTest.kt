package com.example.userapp

import com.example.userapp.domain.user.dto.UserDTO
import com.example.userapp.domain.user.entity.User
import com.example.userapp.domain.user.exception.UserNotFoundException
import com.example.userapp.domain.user.exception.UsernameAlreadyExistsException
import com.example.userapp.domain.user.mapper.UserMapper
import com.example.userapp.domain.user.repository.UserRepository
import com.example.userapp.domain.user.service.UserService
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class UserServiceTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userMapper = mockk<UserMapper>()
    val userService = UserService(userRepository, passwordEncoder, userMapper)

    val userId = 1L
    val userDTO = UserDTO(userId, "John", "john123", "password", "ROLE_USER")
    val user = User(userId, "John", "john123", "password", "ROLE_USER")
    val encodedPassword = "encryptedPassword"
    beforeEach {
        every { passwordEncoder.encode(any()) } returns encodedPassword
    }

    describe("getAllUsers") {
        it("returns an empty list when no users are found") {
            every { userRepository.findAll() } returns emptyList()

            val result = userService.getAllUsers()

            result shouldBe emptyList<UserDTO>()
            verify { userRepository.findAll() }
        }

        it("returns a list of users") {
            every { userRepository.findAll() } returns listOf(user)
            every { userMapper.fromUser(any()) } returns userDTO

            val result = userService.getAllUsers()

            result.size shouldBe 1
            result[0] shouldBe userDTO
            verify { userRepository.findAll() }
        }
    }

    describe("getUserById") {
        it("throws UserNotFoundException when user is not found") {
            every { userRepository.findById(userId) } returns Optional.empty()

            shouldThrowExactly<UserNotFoundException> {
                userService.getUserById(userId)
            }
        }

        it("returns user when found") {
            every { userRepository.findById(userId) } returns Optional.of(user)
            every { userMapper.fromUser(user) } returns userDTO

            val result = userService.getUserById(userId)

            result shouldBe userDTO
        }
    }

    describe("createUser") {
        it("throws UsernameAlreadyExistsException if username exists") {
            every { userRepository.existsByUsername(userDTO.username) } returns true

            shouldThrowExactly<UsernameAlreadyExistsException> {
                userService.createUser(userDTO)
            }
        }

        it("successfully creates a new user") {
            every { userRepository.existsByUsername(userDTO.username) } returns false
            every { userMapper.toUser(userDTO) } returns user
            every { passwordEncoder.encode("password") } returns "encodedPassword"
            every { userRepository.save(any()) } returns user
            every { userMapper.fromUser(user) } returns userDTO

            val result = userService.createUser(userDTO)

            result shouldBe userDTO
            verify { userRepository.save(any()) }
        }
    }

    describe("updateUser") {
        it("updates an existing user") {
            every { userRepository.findById(userId) } returns Optional.of(user)
            every { userMapper.fromUser(any()) } returns userDTO

            val result = userService.updateUser(userId, userDTO)

            result shouldBe userDTO
        }

        it("throws UserNotFoundException if user does not exist") {
            every { userRepository.findById(userId) } returns Optional.empty()

            shouldThrowExactly<UserNotFoundException> {
                userService.updateUser(userId, userDTO)
            }
        }
    }

    describe("deleteUser") {
        it("deletes a user successfully") {
            every { userRepository.existsById(userId) } returns true
            justRun { userRepository.deleteById(userId) }

            userService.deleteUser(userId)

            verify { userRepository.deleteById(userId) }
        }

        it("throws UserNotFoundException if user does not exist") {
            every { userRepository.existsById(userId) } returns false

            shouldThrowExactly<UserNotFoundException> {
                userService.deleteUser(userId)
            }
        }
    }
})
