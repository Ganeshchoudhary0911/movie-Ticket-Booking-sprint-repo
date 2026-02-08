package com.cg.controller;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal principal) {
        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        model.addAttribute("user", opt.get());
        return "profile-edit";
    }

    @PostMapping("/profile/update")  // <- EXACT mapping Spring is looking for
    public String updateProfile(@ModelAttribute("user") @Valid User updated,
                                Principal principal) {
        Optional<User> opt = userRepository.findByUsername(principal.getName());
        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = opt.get();
        // Update only the fields you allow to change
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        user.setPhoneNumber(updated.getPhoneNumber());
        userRepository.save(user);

        return "redirect:/profile/edit?success=true";
    }
}






























//package com.cg.controller;
//
//import com.cg.entity.Booking;
//import com.cg.entity.User;
//import com.cg.repository.UserRepository;
//import com.cg.service.BookingService;
//
//import jakarta.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import java.security.Principal;
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Controller
//public class ProfileController {
//
//    private final UserRepository userRepository;
//    
//    @Autowired
//	private BookingService bookingService;
//
//    public ProfileController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping("/profile/edit")
//    public String editProfile(Model model, Principal principal) {
//
//        Optional<User> opt = userRepository.findByUsername(principal.getName());
//        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
//        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";
//
//        model.addAttribute("user", opt.get());
//        return "profile-edit";
//    }
//
//    @PostMapping("/profile/update")
//    public String updateProfile(@ModelAttribute("user") @Valid User updated,
//                                Principal principal) {
//
//        Optional<User> opt = userRepository.findByUsername(principal.getName());
//        if (opt.isEmpty()) opt = userRepository.findByEmail(principal.getName());
//        if (opt.isEmpty()) return "redirect:/login?error=unauthorized";
//
//        User user = opt.get();
//        user.setUsername(updated.getUsername());
//        user.setEmail(updated.getEmail());
//        user.setPhoneNumber(updated.getPhoneNumber());
//
//        userRepository.save(user);
//
//        return "redirect:/profile/edit?success=true";
//    }
//    
//    @GetMapping("/bookings/history")
//    public String bookingHistory(Model model) {
//        User user = getCurrentUser();
//        if (user == null) return "redirect:/login?error=unauthorized";
//
//        List<Booking> all = bookingService.getUserBookings(user);
//
//        List<Booking> history = all.stream()
//                .filter(b -> !eq(b.getBookingStatus(), "CONFIRMED") && !eq(b.getPaymentStatus(), "PAID"))
//                .sorted(Comparator.comparing(Booking::getBookingDate, Comparator.nullsLast(LocalDate::compareTo)).reversed())
//                .collect(Collectors.toList());
//
//        model.addAttribute("bookings", history);
//        return "/bookings-history";
//    }
//
//    /** Resolve current user without throwing; returns null if not found */
//    private User getCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) return null;
//
//        String name = auth.getName();
//        if (auth.getPrincipal() instanceof UserDetails ud) {
//            name = ud.getUsername();
//        }
//
//        Optional<User> byUsername = userRepository.findByUsername(name);
//        if (byUsername.isPresent()) return byUsername.get();
//
//        return userRepository.findByEmail(name).orElse(null);
//    }
//
//    private static boolean eq(String a, String b) {
//        return a != null && a.equalsIgnoreCase(b);
//    }
//}