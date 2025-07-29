package com.example.ss12.security;

import com.example.ss12.security.exception.AccessDeniedHandler;
import com.example.ss12.security.exception.AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // @bean passwordEncoder() {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception{
        return auth.getAuthenticationManager();
    }

    // Phan quyen truy cap
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request->{
                            request.requestMatchers("/api/v1/auth/**").permitAll() // Cho phép truy cập không cần xác thực
                                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Chỉ cho phép người dùng có vai trò ADMIN truy cập
                                    .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")  //chi  cho phép người dùng có vai trò USER hoặc ADMIN truy cập
                                    .requestMatchers("/api/v1/moderator/**").hasAuthority("ROLE_MODERATOR") // Chỉ cho phép người dùng có vai trò MODERATOR truy cập
                                    .anyRequest().authenticated(); // Tất cả các yêu cầu khác đều cần xác thực
                        }
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(ex->
                        ex.authenticationEntryPoint(new AuthenticationEntryPoint() {
                                })
                                .accessDeniedHandler(new AccessDeniedHandler())
                );
        return http.build();
    }
}
