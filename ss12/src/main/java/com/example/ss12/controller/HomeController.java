package com.example.ss12.controller;

import com.example.ss12.security.principal.UserDetailsCus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/api/v1/admin/home")
    @ResponseStatus(HttpStatus.OK)
    public String home(@AuthenticationPrincipal UserDetailsCus userDetailsCus) {
        return "Welcome to the Admin Home Page!";
    }
}
