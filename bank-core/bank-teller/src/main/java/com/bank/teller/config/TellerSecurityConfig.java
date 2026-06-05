package com.bank.teller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TellerSecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain tellerFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/teller/**")
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}