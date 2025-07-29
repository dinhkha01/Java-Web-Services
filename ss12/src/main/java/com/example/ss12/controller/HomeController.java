package com.example.ss12.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/api/v1/admin/home")
    @ResponseStatus(HttpStatus.OK)
    public String home() {
        return "Welcome to the Admin Home Page!";
    }
}
