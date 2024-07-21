package com.example.userapp.domain.user.service

import com.example.userapp.auth.CustomUserDetails
import com.example.userapp.domain.user.entity.User
import com.example.userapp.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return CustomUserDetails(user)
    }
}
