package com.example.ss12.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Bt1 {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! This is a secured endpoint.";
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the home page! (Secured)";
    }

    @GetMapping("/public/info")
    public String publicInfo() {
        return "This is public information - no authentication required!";
    }

    @GetMapping("/public/about")
    public String publicAbout() {
        return "About page - accessible to everyone!";
    }

    @GetMapping("/public/contact")
    public String publicContact() {
        return "Contact us: info@example.com (Public endpoint)";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard - Authentication required!";
    }
}