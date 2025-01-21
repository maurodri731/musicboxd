package com.mau.musicboxd.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository){
        return args -> {
            User mauri = new User(
                "mauri",
                "mau@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
            );
            User mau = new User(
                "mau",
                "m@gmail.com",
                LocalDate.of(2005, Month.JANUARY, 5)
            );
            repository.saveAll(List.of(mauri, mau));
        };
    }
}
