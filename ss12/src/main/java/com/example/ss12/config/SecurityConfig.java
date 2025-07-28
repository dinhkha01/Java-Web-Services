package com.example.ss12.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Cho phép truy cập tự do vào các endpoint /public/**
                        .requestMatchers("/public/**").permitAll()

                        // Tất cả các request khác đều yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Cấu hình trang login mặc định
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        // Cấu hình logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/public/info")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        // Tắt CSRF cho demo (trong production nên bật)
                        .disable()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Tạo user mẫu cho việc test
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}