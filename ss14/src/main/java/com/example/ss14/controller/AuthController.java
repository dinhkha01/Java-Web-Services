package com.example.ss14.controller;

import com.example.ss14.model.dto.request.FormLogin;
import com.example.ss14.model.dto.request.FormRegister;
import com.example.ss14.model.dto.response.JwtResponse;
import com.example.ss14.model.entity.User;
import com.example.ss14.security.principal.UserDetailsCus;
import com.example.ss14.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> login(@RequestBody FormLogin request){
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<User> register(@RequestBody FormRegister request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }
    @GetMapping("/hello")
    public ResponseEntity<String> hello(@AuthenticationPrincipal UserDetailsCus userDetails) {
        String message = String.format("Hello %s! You are authenticated successfully.",
                userDetails.getUsername());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
