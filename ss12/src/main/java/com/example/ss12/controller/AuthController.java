package com.example.ss12.controller;

import com.example.ss12.model.dto.request.FormLogin;
import com.example.ss12.model.dto.request.FormRegister;
import com.example.ss12.model.entity.Account;
import com.example.ss12.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<Account> login(@RequestBody FormLogin request){
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<Account> register(@RequestBody FormRegister request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }
}
