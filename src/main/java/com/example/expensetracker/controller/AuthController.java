package com.example.expensetracker.controller;

import com.example.expensetracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        boolean success = userService.register(username, password);

        if (!success) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        return "redirect:/login";
    }
}
