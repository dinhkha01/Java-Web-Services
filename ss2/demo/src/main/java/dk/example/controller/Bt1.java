package dk.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Bt1 {
    @GetMapping("/bt1")
    public String showBt() {
        return "bt1";
    }

}
