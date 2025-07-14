package dk.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Bt3 {
    @GetMapping("/hello")
    public String hello() {
        return "bt3/hello";
    }
}
