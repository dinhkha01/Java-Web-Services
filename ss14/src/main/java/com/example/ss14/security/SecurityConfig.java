package com.example.ss14.security;

import com.example.ss14.security.exception.AccessDeniedHandler;
import com.example.ss14.security.exception.AuthenticationEntryPoint;
import com.example.ss14.security.jwt.JWTAuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Cấu hình bảo mật Spring Security với JWT Authentication
 *
 * Logic phân quyền:
 * 1. /api/v1/auth/** - Public: Cho phép tất cả truy cập (đăng ký, đăng nhập)
 * 2. /api/v1/login - Public: Endpoint đăng nhập
 * 3. /api/v1/movies/** - Public: Xem danh sách phim, tìm kiếm (GET only)
 * 4. /api/v1/showtimes/** - Public: Xem lịch chiếu (GET only)
 * 5. /api/v1/admin/movies/** - ADMIN only: CRUD phim
 * 6. /api/v1/admin/showtimes/** - ADMIN only: CRUD lịch chiếu
 * 7. /api/v1/admin/** - ADMIN only: Tất cả admin endpoints khác
 * 8. /api/v1/user/** - USER & ADMIN: User có role USER hoặc ADMIN
 * 9. /api/v1/moderator/** - MODERATOR only: Chỉ user có authority ROLE_MODERATOR
 * 10. /api/v1/hello - Authenticated: Yêu cầu xác thực JWT
 * 11. Các endpoint khác - Authenticated: Yêu cầu xác thực
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthTokenFilter jwtAuthTokenFilter;

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
        return http
                // Tắt CORS và CSRF (thường dùng cho REST API)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình phân quyền truy cập
                .authorizeHttpRequests(request -> request
                        // 1. PUBLIC ENDPOINTS - Không cần xác thực
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/login").permitAll()  // Endpoint login public
                        .requestMatchers("/public/**").permitAll()

                        // 2. MOVIE ENDPOINTS - Public cho GET, ADMIN cho POST/PUT/DELETE
                        .requestMatchers("GET", "/api/v1/movies", "/api/v1/movies/**").permitAll()
                        .requestMatchers("POST", "/api/v1/admin/movies").hasRole("ADMIN")
                        .requestMatchers("PUT", "/api/v1/admin/movies/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/v1/admin/movies/**").hasRole("ADMIN")

                        // 3. SHOWTIME ENDPOINTS - Public cho GET, ADMIN cho POST/PUT/DELETE
                        .requestMatchers("GET", "/api/v1/showtimes", "/api/v1/showtimes/**").permitAll()
                        .requestMatchers("POST", "/api/v1/admin/showtimes").hasRole("ADMIN")
                        .requestMatchers("PUT", "/api/v1/admin/showtimes/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/v1/admin/showtimes/**").hasRole("ADMIN")

                        // 4. ADMIN ENDPOINTS - Chỉ role ADMIN
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // 5. USER & ADMIN - Role USER hoặc ADMIN
                        .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")

                        // 6. MODERATOR ONLY - Authority ROLE_MODERATOR
                        .requestMatchers("/api/v1/moderator/**").hasAuthority("ROLE_MODERATOR")

                        // 7. HELLO ENDPOINT - Yêu cầu xác thực (bất kỳ role nào)
                        .requestMatchers("/api/v1/hello").authenticated()

                        // 8. Tất cả endpoints khác cần xác thực
                        .anyRequest().authenticated()
                )

                // Cấu hình session STATELESS (cho REST API)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)

                // Đăng ký Authentication Provider
                .authenticationProvider(authenticationProvider())

                // Xử lý exception
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new AuthenticationEntryPoint())
                        .accessDeniedHandler(new AccessDeniedHandler())
                )
                .build();
    }
}