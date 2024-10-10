package com.aya.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Enables Spring Security’s web security support and provides the Spring MVC integration.
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter; //  A custom filter for handling JWT authentication.
    private final AuthenticationProvider authenticationProvider; // A component that performs authentication logic.


    // This method configures the security filter chain, defining how HTTP requests are secured.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * Disables Cross-Site Request Forgery (CSRF) protection,
                 * as it's no longer needed when the server does not maintain a user session.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                // Starts configuring authorization for incoming HTTP requests.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/v1/auth/**") // Specifies URL patterns to apply specific security rules.
                        .permitAll() // On these specific URL patters ALLOW unrestricted access.
                        .anyRequest() // Any request that does not match the previously specified patterns
                        .authenticated() // Require authentication for them
                )


                /*
                 * And configure how Spring Security manages HTTP sessions.
                 * Spring Security will not create or use an HTTP session. Every request must be authenticated independently.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )


                /*
                 * Adds the JWTAuthenticationFilter before the UsernamePasswordAuthenticationFilter in the security filter chain
                 * to Ensures that JWT authentication is processed before Spring Security's default authentication filter.
                 */
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
