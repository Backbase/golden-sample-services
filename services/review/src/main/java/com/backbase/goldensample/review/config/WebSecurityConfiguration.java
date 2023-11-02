package com.backbase.goldensample.review.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;

@Validated
@EnableWebSecurity
@Configuration
@ConditionalOnProperty(prefix = "security", name = "disabled")
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain emptySecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        return http.build();
    }
}
