package com.example.ss14.security.principal;

import com.example.ss14.model.entity.User;
import com.example.ss14.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceCus implements UserDetailsService {
    @Autowired
    private IAccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = accountRepository.loadUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole().name()));
        return UserDetailsCus.builder()
                .id(account.getId())
                .username(username)
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}
