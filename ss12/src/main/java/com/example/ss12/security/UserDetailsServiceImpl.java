package com.example.ss12.security;

import com.example.ss12.model.entity.Account;
import com.example.ss12.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Service để load thông tin user từ database cho Spring Security
 * Implement UserDetailsService để tích hợp với hệ thống xác thực của Spring Security
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IAccountRepository accountRepository;

    /**
     * Load user details từ database dựa trên username/email/phone
     *
     * @param username có thể là username, email hoặc số điện thoại
     * @return UserDetails object chứa thông tin user và authorities
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong database (có thể tìm bằng username, email hoặc phone)
        Account account = accountRepository.loadUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy user với username/email/phone: " + username));

        // Chuyển đổi roles thành authorities cho Spring Security
        Collection<GrantedAuthority> authorities = account.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        // Tạo và trả về UserDetails object
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

/*
 * GIẢI THÍCH CÁCH HOẠT ĐỘNG:
 *
 * 1. Khi user đăng nhập, Spring Security sẽ gọi loadUserByUsername()
 * 2. Method này tìm Account trong database qua username/email/phone
 * 3. Chuyển đổi Set<Role> thành Collection<GrantedAuthority>
 * 4. Tạo UserDetails object với thông tin user và quyền hạn
 * 5. Spring Security sử dụng UserDetails này để xác thực và phân quyền
 *
 * VÍ DỤ AUTHORITIES:
 * - User có role ROLE_USER → authority "ROLE_USER"
 * - User có role ROLE_ADMIN → authority "ROLE_ADMIN"
 * - User có role ROLE_MODERATOR → authority "ROLE_MODERATOR"
 *
 * LƯU Ý:
 * - Repository query hỗ trợ tìm bằng username, email hoặc phone
 * - Tất cả account mặc định là enabled, không expired, không locked
 * - Authorities được tạo từ role name với prefix "ROLE_"
 */