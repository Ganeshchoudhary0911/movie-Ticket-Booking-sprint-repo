package com.cg.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	public String confirmBooking(@PathVariable Long showId, @RequestParam double amount, Principal principal,
			Model model) {

		User user = userService.findByUsername(principal.getName());
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
		return "redirect:/history";
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