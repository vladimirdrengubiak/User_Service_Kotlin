package com.example.userapp.config

import com.example.userapp.domain.user.entity.User
import com.example.userapp.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    @Bean
    fun initDatabase(): CommandLineRunner = CommandLineRunner {
        if (userRepository.count() == 0L) {
            logger.info("Initializing database with sample data.")
            val users = listOf(
                User(null, "Ferko", "ferko", passwordEncoder.encode("Ferko123"), "ROLE_USER"),
                User(null, "Jozko", "jozko", passwordEncoder.encode("Jozko123"), "ROLE_USER"),
                User(null, "John", "john", passwordEncoder.encode("John123"), "ROLE_USER"),
                User(null, "Jane", "jane", passwordEncoder.encode("Jane123"), "ROLE_USER"),
                User(null, "admin", "admin", passwordEncoder.encode("Admin123"), "ROLE_ADMIN"),
                User(null, "user", "user", passwordEncoder.encode("User123"), "ROLE_USER")
            )
            userRepository.saveAll(users)
            logger.info("Sample data initialized.")
        } else {
            logger.info("Database already initialized.")
        }
    }
}
