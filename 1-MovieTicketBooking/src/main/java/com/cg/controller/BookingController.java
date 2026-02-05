package com.cg.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.entity.*;
import com.cg.service.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	private ShowService showService;
	private UserService userService;

	public BookingController(BookingService bookingService, ShowService showService, UserService userService) {
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

	    // 1) Resolve current user by username or email (no exceptions)
	    Optional<User> optUser = userService.findByUsername(principal.getName());
	    if (optUser.isEmpty()) {
	        optUser = userService.findByEmail(principal.getName());
	    }
	    if (optUser.isEmpty()) {
	        return "redirect:/login?error=unauthorized";
	    }
	    User user = optUser.get();

	    // 2) Load the Show
	    Show show = showService.getShowById(showId);
	    if (show == null) {
	        ra.addFlashAttribute("error", "Show not found");
	        return "redirect:/";
	    }

	    // 3) Create booking
	    Booking booking = bookingService.createBooking(user, show, amount);

	    // 4) Redirect to payment page (or success)
	    ra.addFlashAttribute("booking", booking);
	    return "redirect:/payment?bookingId=" + booking.getBookingId();
	}
	

	// Booking payment success
	@GetMapping("/success/{bookingId}")
	public String paymentSuccess(@PathVariable Long bookingId, Model model) {

		Booking booking = bookingService.confirmPayment(bookingId);

		model.addAttribute("booking", booking);
		return "success";
	}

	// Booking payment failure S
	@GetMapping("/fail/{bookingId}")
	public String paymentFail(@PathVariable Long bookingId, Model model, Principal principal) {
		Booking existing = bookingService.getBookingById(bookingId);
		if (existing == null)
			return "error";

		// Optional: ensure current user owns this booking
		if (!existing.getUser().getUsername().equals(principal.getName()))
			return "error";

		// Mark FAILED + CANCELLED
		Booking booking = bookingService.failPayment(bookingId);
		model.addAttribute("booking", booking);
		return "payment-fail"; // Thymeleaf template: payment-fail.html
	}

	/** User-initiated cancel (e.g., before payment or within policy window) */
	@PostMapping("/cancel/{bookingId}")
	public String cancelBooking(@PathVariable Long bookingId, Principal principal, Model model) {
		String username = principal.getName();

		Booking booking = bookingService.cancelBooking(bookingId, username);
		if (booking == null)
			return "error";

		model.addAttribute("booking", booking);
		// Redirect to history or a cancel confirmation page
		return "redirect:/bookings-history";
	}

	private static boolean equalsIgnoreCase(String a, String b) {
	    return a != null && a.equalsIgnoreCase(b);
	}
	
	@GetMapping("/bookings/current")
	public String currentBookings(Model model, Principal principal) {
	    Optional<User> optUser = userService.findByUsername(principal.getName());
	    if (optUser.isEmpty()) {
	        optUser = userService.findByEmail(principal.getName());
	    }
	    if (optUser.isEmpty()) {
	        return "redirect:/login?error=unauthorized";
	    }
	    User user = optUser.get();

	    List<Booking> bookings = bookingService.getUserBookings(user);

	    // "current" = CONFIRMED or PAID (status-based)
	    List<Booking> current = bookings.stream()
	        .filter(b -> equalsIgnoreCase(b.getBookingStatus(), "CONFIRMED")
	                  || equalsIgnoreCase(b.getPaymentStatus(), "PAID"))
	        .sorted(Comparator.comparing(Booking::getBookingDate,
	                 Comparator.nullsLast(LocalDate::compareTo)).reversed())
	        .toList();

	    model.addAttribute("bookings", current);
	    return "bookings-current";
	}

	@GetMapping("/profile/edit")
	public String editProfile(Model model, Principal principal) {
	    Optional<User> optUser = userService.findByUsername(principal.getName());
	    if (optUser.isEmpty()) {
	        optUser = userService.findByEmail(principal.getName());
	    }
	    if (optUser.isEmpty()) {
	        return "redirect:/login?error=unauthorized";
	    }
	    model.addAttribute("user", optUser.get());
	    return "profile-edit";
	}

}