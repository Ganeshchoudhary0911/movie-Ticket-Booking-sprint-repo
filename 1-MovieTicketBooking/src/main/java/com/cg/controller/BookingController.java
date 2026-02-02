package com.cg.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cg.entity.*;
import com.cg.service.*;

@Controller
@RequestMapping("/bookings")
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
                                 Model model) {

        User user = userService.findByUserName(principal.getName());
        Show show = showService.getShowById(showId);

        if (user == null || show == null) {
            return "error";
        }

        Booking booking = bookingService.createBooking(user, show, amount);

        model.addAttribute("booking", booking);
        return "payment"; // show payment page
    }

    // Booking payment success
    @GetMapping("/success/{bookingId}")
    public String paymentSuccess(@PathVariable Long bookingId, Model model) {

        Booking booking = bookingService.confirmPayment(bookingId);

        model.addAttribute("booking", booking);
        return "success";
    }

    // Booking history
    @GetMapping("/history")
    public String bookingHistory(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        List<Booking> bookings = bookingService.getUserBookings(user);

        model.addAttribute("bookings", bookings);
        return "history";
    }
}