package com.aya.security.config;

import com.aya.security.exception.UserNameNotFoundException;
import com.aya.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserManagementApplicationConfig {

    private final UserRepository userRepository;

    // @Bean -> is needed to tell spring that this method represents a bean, and a bean has to always be public
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username) // findByEmail(username) returns Optional so orElseThrow is needed
                .orElseThrow(() -> new UserNameNotFoundException("User not found!"));
    }

    // Data Access Object which is responsible to fetch user details and encode password, etc
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Specify which User Detail Service to use to fetch user's data
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Specify the Algorithm to encode password
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
