package com.cg.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.entity.*;
import com.cg.service.*;

@Controller
@RequestMapping("/bookings")   // CLASS-LEVEL PREFIX
public class BookingController {

    private final BookingService bookingService;
    private final ShowService showService;
    private final UserService userService;

    public BookingController(BookingService bookingService,
                             ShowService showService,
                             UserService userService) {
        this.bookingService = bookingService;
        this.showService = showService;
        this.userService = userService;
    }

    // Create booking → goes to /payment page
    @PostMapping("/confirm/{showId}")
    public String confirmBooking(@PathVariable Long showId,
                                 @RequestParam double amount,
                                 Principal principal,
                                 RedirectAttributes ra) {

        Optional<User> optUser = userService.findByUsername(principal.getName());
        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = optUser.get();

        Show show = showService.getShowById(showId);
        if (show == null) {
            ra.addFlashAttribute("error", "Show not found");
            return "redirect:/";
        }

        Booking booking = bookingService.createBooking(user, show, amount);

        ra.addFlashAttribute("booking", booking);
        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }


    @GetMapping("/success/{bookingId}")
    public String paymentSuccess(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.confirmPayment(bookingId);
        model.addAttribute("booking", booking);
        return "success";
    }

    @GetMapping("/fail/{bookingId}")
    public String paymentFail(@PathVariable Long bookingId, Model model, Principal principal) {
        Booking existing = bookingService.getBookingById(bookingId);
        if (existing == null) return "error";

        if (!existing.getUser().getUsername().equals(principal.getName()))
            return "error";

        Booking booking = bookingService.failPayment(bookingId);
        model.addAttribute("booking", booking);
        return "payment-fail";
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {
        String username = principal.getName();
        Booking booking = bookingService.cancelBooking(bookingId, username);
        if (booking == null) return "error";

        return "redirect:/bookings/history";
    }

    private static boolean eq(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    // --------------------------------------------------------
    // ❗ FIXED: Correct URLs (only method-level paths needed)
    // --------------------------------------------------------

    @GetMapping("/current")   // final URL = /bookings/current   ✔ CORRECT
    public String currentBookings(Model model, Principal principal) {

        Optional<User> optUser = userService.findByUsername(principal.getName());
        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = optUser.get();

        List<Booking> bookings = bookingService.getUserBookings(user);
        List<Booking> current = bookings.stream()
                .filter(b -> eq(b.getBookingStatus(), "CONFIRMED") ||
                             eq(b.getPaymentStatus(), "PAID"))
                .sorted(Comparator.comparing(Booking::getBookingDate,
                         Comparator.nullsLast(LocalDate::compareTo)).reversed())
                .toList();

        model.addAttribute("bookings", current);
        return "bookings-current";
    }


    @GetMapping("/history")  // final URL = /bookings/history  ✔ CORRECT
    public String bookingHistory(Model model, Principal principal) {

        Optional<User> optUser = userService.findByUsername(principal.getName());
        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";

        User user = optUser.get();

        List<Booking> bookings = bookingService.getUserBookings(user);
        List<Booking> history = bookings.stream()
                .filter(b -> !eq(b.getBookingStatus(), "CONFIRMED") &&
                             !eq(b.getPaymentStatus(), "PAID"))
                .sorted(Comparator.comparing(Booking::getBookingDate,
                         Comparator.nullsLast(LocalDate::compareTo)).reversed())
                .toList();

        model.addAttribute("bookings", history);
        return "bookings-history";
    }
}