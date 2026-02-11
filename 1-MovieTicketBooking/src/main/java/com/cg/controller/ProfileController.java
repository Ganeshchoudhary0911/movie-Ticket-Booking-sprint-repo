package com.cg.controller;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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

    /** Show Edit Profile page */
    @GetMapping("/profile/edit")
    public String editProfile(Model model,
                              Principal principal,
                              @RequestParam(value = "success", required = false) String successParam) {
        if (principal == null) return "redirect:/login?error=unauthorized";

        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", opt.get());
        }
        if (successParam != null && !model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", "Your profile was updated successfully.");
        }
        return "profile-edit";
    }

    /** Update profile (PUT) */
    @PutMapping("/profile")
    public String updateProfile(@ModelAttribute("user") @Valid User updated,
                                Principal principal,
                                RedirectAttributes ra) {
        if (principal == null) return "redirect:/login?error=unauthorized";

        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = opt.get();

        // Only update allowed fields (do not allow client to change role/enabled/password here)
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        user.setPhoneNumber(updated.getPhoneNumber());

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            // Likely due to unique constraints on username/email
            ra.addFlashAttribute("errorMessage", "Username or email already exists. Please choose another.");
            ra.addFlashAttribute("user", updated);
            return "redirect:/profile/edit";
        }

        ra.addFlashAttribute("successMessage", "Your profile was updated successfully.");
        return "redirect:/profile/edit";
        // or: return "redirect:/profile/edit?success=true";
    }

}