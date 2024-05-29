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

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private UserRepository userRepository;
    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Bean
    public CommandLineRunner initDatabase() {
        return (args) -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User("Ferko"));
                userRepository.save(new User("Jozko"));
                userRepository.save(new User("John"));
                userRepository.save(new User("Jane"));
            }
        };
    }

}
