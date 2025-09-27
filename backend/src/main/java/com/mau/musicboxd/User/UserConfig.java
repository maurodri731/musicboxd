package com.mau.musicboxd.User;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository, PasswordEncoder passwordEncoder){
        return args -> {
            User mauri = new User(
                "mauri",
                "mau@gmail.com",
                passwordEncoder.encode("1233456")
            );
            repository.saveAll(List.of(mauri));
        };
    }
}
