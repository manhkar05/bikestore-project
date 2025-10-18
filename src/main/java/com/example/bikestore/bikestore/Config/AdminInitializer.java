package com.example.bikestore.bikestore.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.bikestore.bikestore.Models.User;
import com.example.bikestore.bikestore.Repositories.UserRepository;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdminIfMissing(UserRepository userRepository) {
        return args -> {
            // Create a default admin if none exists
            String adminEmail = "admin@bikestore.com";
            if (userRepository.findByEmail(adminEmail) == null) {
                User admin = new User();
                admin.setName("admin");
                admin.setEmail(adminEmail);
                String rawPassword = "admin123"; // change after first run in production
                admin.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
                admin.setRole("admin");
                admin.setAddress("");
                admin.setPhone("");
                userRepository.save(admin);
                System.out.println("[AdminInitializer] Created default admin: " + adminEmail + " / " + rawPassword);
            }
        };
    }
}
