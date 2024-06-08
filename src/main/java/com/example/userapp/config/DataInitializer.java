package com.example.userapp.config;


import com.example.userapp.model.User;
import com.example.userapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return (args) -> {
            if (userRepository.count() == 0) {
                logger.info("Initializing database with sample data.");
                userRepository.saveAll(List.of(
                        new User(null,"Ferko", "ferko", passwordEncoder.encode("Ferko123"), "ROLE_USER"),
                        new User(null,"Jozko", "jozko", passwordEncoder.encode("Jozko123"), "ROLE_USER"),
                        new User(null,"John", "john", passwordEncoder.encode("John123"), "ROLE_USER"),
                        new User(null,"Jane", "jane", passwordEncoder.encode("Jane123"), "ROLE_USER"),
                        new User(null,"admin", "admin", passwordEncoder.encode("Admin123"), "ROLE_ADMIN"),
                        new User(null,"user", "user", passwordEncoder.encode("User123"), "ROLE_USER")
                ));
                logger.info("Sample data initialized.");
            } else {
                logger.info("Database already initialized.");
            }
        };
    }

}
