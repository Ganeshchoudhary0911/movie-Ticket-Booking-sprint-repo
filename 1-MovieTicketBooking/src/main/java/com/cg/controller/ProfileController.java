package com.cg.controller;

import com.cg.entity.Booking;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import com.cg.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public ProfileController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login?error=unauthorized";
        model.addAttribute("user", user);
        return "/profile-edit";
    }

    @GetMapping("/bookings/current")
    public String currentBookings(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login?error=unauthorized";

        List<Booking> all = bookingService.getUserBookings(user);

        // Status-based classification (works without Show date/time)
        List<Booking> current = all.stream()
                .filter(b -> eq(b.getBookingStatus(), "CONFIRMED") || eq(b.getPaymentStatus(), "PAID"))
                .sorted(Comparator.comparing(Booking::getBookingDate, Comparator.nullsLast(LocalDate::compareTo)).reversed())
                .collect(Collectors.toList());

        model.addAttribute("bookings", current);
        return "/bookings-current";
    }

    @GetMapping("/bookings/history")
    public String bookingHistory(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login?error=unauthorized";

        List<Booking> all = bookingService.getUserBookings(user);

        List<Booking> history = all.stream()
                .filter(b -> !eq(b.getBookingStatus(), "CONFIRMED") && !eq(b.getPaymentStatus(), "PAID"))
                .sorted(Comparator.comparing(Booking::getBookingDate, Comparator.nullsLast(LocalDate::compareTo)).reversed())
                .collect(Collectors.toList());

        model.addAttribute("bookings", history);
        return "/bookings-history";
    }

    /** Resolve current user without throwing; returns null if not found */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        String name = auth.getName();
        if (auth.getPrincipal() instanceof UserDetails ud) {
            name = ud.getUsername();
        }

        Optional<User> byUsername = userRepository.findByUsername(name);
        if (byUsername.isPresent()) return byUsername.get();

        return userRepository.findByEmail(name).orElse(null);
    }

    private static boolean eq(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }
}
