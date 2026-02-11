package com.cg.controller;
 
import com.cg.dto.SignupDto;
import com.cg.service.IUserService;
import com.cg.service.UserService;

import jakarta.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
 
@Controller
public class AuthController {
 
    @Autowired
    private UserService userService;
 
    // Constructor injection is fine too
    public AuthController(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("user", new SignupDto()); // th:object="${user}"
        return "signup";
    }
 
    @PostMapping("/signup")
    public String doSignup(@Valid @ModelAttribute("user") SignupDto signupDto,
                           BindingResult result,
                           Model model) {
    	// Bean validation failures?
	    if (result.hasErrors()) {
	        return "signup";
	    }
 
        if (result.hasErrors()) {
            return "signup";
        }
 
        if (userService.existsByUsername(signupDto.getUsername())) {
            result.rejectValue("username", "exists", "Username already taken");
            return "signup";
        }
 
        if (userService.existsByEmail(signupDto.getEmail())) {
            result.rejectValue("email", "exists", "Email already registered");
            return "signup";
        }
 
        // Delegate creation to service (handles encoding + defaults)
        userService.createUser(signupDto);
 
        return "redirect:/login?signup=true";
    }
}