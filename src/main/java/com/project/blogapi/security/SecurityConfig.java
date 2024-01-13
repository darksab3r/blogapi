package com.project.blogapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private TokenService tokenService;

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable()).authorizeRequests()
                .requestMatchers(HttpMethod.GET,"/articles").permitAll()
                .requestMatchers(HttpMethod.POST,"/users/signup").permitAll()
                .requestMatchers(HttpMethod.POST,"/users/login").permitAll()
                .requestMatchers(HttpMethod.GET,"/h2-console/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(new UserAuthenticationFilter(tokenService), AnonymousAuthenticationFilter.class);
        return http.build();
    }
}
