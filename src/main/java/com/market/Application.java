package com.market;

import com.market.config.CustomUserDetails;
import com.market.entity.User;
import com.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder,
                                      UserRepository userRepository) throws Exception{
        userRepository.save(new User(1L,"user","user","+380935158848", User.Role.USER));
        authenticationManagerBuilder
                .userDetailsService(s -> new CustomUserDetails(userRepository
                        .findByUsername(s)));
    }
}
