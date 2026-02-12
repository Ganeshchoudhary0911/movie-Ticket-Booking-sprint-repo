package com.cg.controller;

import com.cg.entity.User;
import com.cg.service.IUserService;
import com.cg.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserService userService,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model,
                              Principal principal,
                              @RequestParam(value = "success", required = false) String successParam) {

        if (principal == null) {
            return "redirect:/login?error=unauthorized";
        }

        Optional<User> opt = userService.findEntityByUsernameOrEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        model.addAttribute("user", opt.get());

        if (successParam != null && !model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", "Your profile was updated successfully.");
        }

        return "profile-edit";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") @Valid User updated,
                                Principal principal,
                                RedirectAttributes ra) {

        if (principal == null) {
            return "redirect:/login?error=unauthorized";
        }

        Optional<User> opt = userService.findEntityByUsernameOrEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = opt.get();

        // Update simple fields
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        user.setPhoneNumber(updated.getPhoneNumber());

        // Update password ONLY if entered
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {

            if (updated.getPassword().length() < 6) {
                ra.addFlashAttribute("errorMessage", "Password must be at least 8 characters.");
                return "redirect:/profile/edit";
            }

            user.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        userService.saveEntity(user);

        ra.addFlashAttribute("successMessage", "Your profile was updated successfully.");
        return "redirect:/profile/edit";
    }
}
