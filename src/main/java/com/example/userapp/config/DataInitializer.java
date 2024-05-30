package com.example.userapp.config;


import com.example.userapp.model.User;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public CommandLineRunner initDatabase() {
        return (args) -> {
            if (userRepository.count() == 0) {
                logger.info("Initializing database with sample data.");
                userRepository.saveAll(List.of(
                        new User("Ferko", "ferko", passwordEncoder.encode("Ferko123"), "ROLE_USER"),
                        new User("Jozko", "jozko", passwordEncoder.encode("Jozko123"), "ROLE_USER"),
                        new User("John", "john", passwordEncoder.encode("John123"), "ROLE_USER"),
                        new User("Jane", "jane", passwordEncoder.encode("Jane123"), "ROLE_USER"),
                        new User("admin", "admin", passwordEncoder.encode("Admin123"), "ROLE_ADMIN"),
                        new User("user", "user", passwordEncoder.encode("User123"), "ROLE_USER")
                ));
                logger.info("Sample data initialized.");
            } else {
                logger.info("Database already initialized.");
            }
        };
    }

}
