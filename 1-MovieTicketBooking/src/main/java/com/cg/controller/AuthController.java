package com.cg.controller;

import com.cg.dto.SignupDto;
import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

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
        // Bind DTO to the form
        model.addAttribute("user", new SignupDto()); // MUST MATCH th:object="${user}"
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@Valid @ModelAttribute("user") SignupDto signupDto,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "signup";
        }

        if (userRepo.existsByUsername(signupDto.getUsername())) {
            result.rejectValue("username", "exists", "Username already taken");
            return "signup";
        }

        if (userRepo.existsByEmail(signupDto.getEmail())) {
            result.rejectValue("email", "exists", "Email already registered");
            return "signup";
        }

        // Map DTO -> Entity and ENCODE the password before saving
        User user = new User();
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(encoder.encode(signupDto.getPassword()));
        user.setPhoneNumber(signupDto.getPhoneNumber());

        // Defaults
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepo.save(user);

        return "redirect:/login?signup=true";
    }
}