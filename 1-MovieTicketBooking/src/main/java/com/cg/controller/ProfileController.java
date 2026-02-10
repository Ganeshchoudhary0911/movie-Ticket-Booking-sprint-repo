package com.cg.controller;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal principal,
                              @RequestParam(value = "success", required = false) String successParam) {
        if (principal == null) {
            return "redirect:/login?error=unauthorized";
        }

        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        model.addAttribute("user", opt.get());

        // Optional: support query param fallback ?success=true
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

        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = opt.get();

        // Update only allowed fields
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        user.setPhoneNumber(updated.getPhoneNumber());

        userRepository.save(user);

        // Flash message survives redirect and is cleared after display
        ra.addFlashAttribute("successMessage", "Your profile was updated successfully.");

        // Redirect back to edit page (canonical)
        return "redirect:/profile/edit";
        // If you prefer the query-param style instead, use:
        // return "redirect:/profile/edit?success=true";
    }
}