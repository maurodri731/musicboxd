package com.mau.musicboxd.User;

import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    //@Bean
    /*CommandLineRunner commandLineRunner(UserRepository repository, PasswordEncoder passwordEncoder){
        return args -> {
            User mauri = new User(
                "mauri",
                "mau@gmail.com",
                passwordEncoder.encode("1233456")
            );
            repository.saveAll(List.of(mauri));
        };
    }*/
}
