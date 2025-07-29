package com.example.ss12.security.principal;

import com.example.ss12.model.entity.Account;
import com.example.ss12.repository.IAccountRepository;
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
        Account account = accountRepository.loadUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<? extends GrantedAuthority> authorities = account.getRoles().stream()
                .map(role-> new SimpleGrantedAuthority(role.getRoleName().name()))
                .toList();
        return UserDetailsCus.builder()
                .id(account.getId())
                .username(username)
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}
