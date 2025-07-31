package com.example.ss14.service;

import com.example.ss14.model.dto.request.FormLogin;
import com.example.ss14.model.dto.request.FormRegister;
import com.example.ss14.model.dto.response.JwtResponse;
import com.example.ss14.model.entity.RoleName;
import com.example.ss14.model.entity.User;
import com.example.ss14.repository.IAccountRepository;
import com.example.ss14.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final IAccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    public JwtResponse login(FormLogin request){
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(authentication);
        User account = accountRepository.loadUserByUsername(request.getUsername()).get();
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(jwtProvider.generateToken((UserDetails) auth.getPrincipal()));
        jwtResponse.setAccount(account);
        return jwtResponse;
    }


    public User register(FormRegister request){
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User account = new User();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setRole(RoleName.ROLE_ADMIN);
        return accountRepository.save(account);

    }
}
