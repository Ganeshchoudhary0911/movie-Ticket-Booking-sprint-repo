package com.cg.controller;

import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder; // injected from SecurityConfig

    // Render login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Render signup page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // Handle signup form submission
    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user) {
        // default role USER
        user.setRole(Role.USER);

        // encode password
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);

        // save to DB
        userService.saveUser(user);

        // redirect to login
        return "redirect:/login";
    }
}
