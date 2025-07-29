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

/**
 * Cấu hình bảo mật Spring Security
 *
 * Logic phân quyền:
 * 1. /api/v1/auth/** - Public: Cho phép tất cả truy cập (đăng ký, đăng nhập)
 * 2. /api/v1/admin/** - ADMIN only: Chỉ user có role ADMIN
 * 3. /api/v1/user/** - USER & ADMIN: User có role USER hoặc ADMIN
 * 4. /api/v1/moderator/** - MODERATOR only: Chỉ user có authority ROLE_MODERATOR
 * 5. Các endpoint khác - Authenticated: Yêu cầu xác thực
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Cấu hình mã hóa mật khẩu bằng BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình Authentication Provider sử dụng DAO
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Cấu hình Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    /**
     * Cấu hình Security Filter Chain với logic phân quyền
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CORS và CSRF (thường dùng cho REST API)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình phân quyền truy cập
                .authorizeHttpRequests(request -> {
                    request
                            // 1. PUBLIC ENDPOINTS - Không cần xác thực
                            .requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers("/public/**").permitAll()

                            // 2. ADMIN ONLY - Chỉ role ADMIN
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                            // 3. USER & ADMIN - Role USER hoặc ADMIN
                            .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")

                            // 4. MODERATOR ONLY - Authority ROLE_MODERATOR
                            .requestMatchers("/api/v1/moderator/**").hasAuthority("ROLE_MODERATOR")

                            // 5. Tất cả endpoints khác cần xác thực
                            .anyRequest().authenticated();
                })

                // Cấu hình session STATELESS (cho REST API)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                        )
                )

                // Đăng ký Authentication Provider
                .authenticationProvider(authenticationProvider())

                // Xử lý exception
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new AuthenticationEntryPoint())
                                .accessDeniedHandler(new AccessDeniedHandler())
                );

        return http.build();
    }
}

