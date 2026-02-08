package com.cg.controller;

import com.cg.entity.*;
import com.cg.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	@Autowired
    UserRepository userRepo;
	
	@Autowired
    PasswordEncoder encoder;
    
    public AuthController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());  // MUST MATCH th:object="${user}"
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "signup";
        }

        if (userRepo.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "exists", "Username already taken");
            return "signup";
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "exists", "Email already registered");
            return "signup";
        }

        // ENCODE THE PASSWORD BEFORE SAVE
        user.setPassword(encoder.encode(user.getPassword()));

        // Set default role
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepo.save(user);

        return "redirect:/login?signup=true";
    }
    
}
