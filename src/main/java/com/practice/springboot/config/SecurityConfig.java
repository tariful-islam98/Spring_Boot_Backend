package com.practice.springboot.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityConfigurerAdapter {

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/**", "/api/post/**", "/api/comment/**", "/api/category/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/post/**", "/api/comment/**").access("@webSecurity.checkUserId(authentication, request)")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**", "/api/post/**", "/api/comment/**").access("@webSecurity.checkUserId(authentication, request)")
                        .requestMatchers("/api/role/**", "/api/privilege/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
    }*/
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/user/**", "/api/post/**", "/api/comment/**", "/api/category/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/post/**", "/api/comment/**").access("@webSecurity.checkUserId(authentication, request)")
//                        .requestMatchers(HttpMethod.PUT, "/api/user/**", "/api/post/**", "/api/comment/**").access("@webSecurity.checkUserId(authentication, request)")
//                        .requestMatchers("/api/role/**", "/api/privilege/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/category/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
//                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
}
