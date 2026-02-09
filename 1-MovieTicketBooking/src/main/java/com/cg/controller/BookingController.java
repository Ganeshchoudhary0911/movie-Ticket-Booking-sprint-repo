package com.cg.controller;
 
import java.security.Principal;

import java.time.LocalDate;

import java.util.Comparator;

import java.util.List;

import java.util.Optional;
 
import com.cg.dto.BookingDto;

import com.cg.dto.ShowDto;

import com.cg.dto.UserDto;

import com.cg.service.BookingService;
import com.cg.service.MovieService;
import com.cg.service.PaymentService;

import com.cg.service.ShowService;

import com.cg.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller

public class BookingController {
	
	@Autowired
	private MovieService movieService;
 
    private final BookingService bookingService;

    private final ShowService showService;

    private final UserService userService;

    private final PaymentService paymentService;

    public BookingController(BookingService bookingService,

                             ShowService showService,

                             UserService userService, PaymentService paymentService) {

        this.bookingService = bookingService;

        this.showService = showService;

        this.userService = userService;

		this.paymentService = paymentService;

    }
 
    // ============================================================================

    // PAYMENT (BOOKING-ONLY FLOW)

    // ============================================================================
 
    // Payment entry: requires bookingId; forwards to /payment/dto

    @GetMapping("/payment")

    public String paymentEntry(@RequestParam(value = "bookingId", required = false) Long bookingId) {

        if (bookingId == null) {

            return "redirect:/history?error=missingBookingId";

        }

        return "forward:/payment/dto?bookingId=" + bookingId;

    }
 
    // Loads the booking and returns the "payment" view

    @GetMapping("/payment/dto")

    public String dtoPayment(@RequestParam Long bookingId, Model model) {

        BookingDto booking = bookingService.getBookingById(bookingId);

        if (booking == null) {

            return "redirect:/history?error=bookingNotFound";

        }

        model.addAttribute("booking", booking);

        return "payment";

    }
 
    // Confirms payment and redirects to success

    @PostMapping("/payment/confirm")

    public String confirmPayment(@RequestParam Long bookingId, RedirectAttributes ra) {

        BookingDto updated = bookingService.confirmPayment(bookingId);

        ra.addFlashAttribute("booking", updated);

        return "redirect:/success/" + bookingId;

    }

    @GetMapping("/payment/fail")

    public String paymentFail() {

        return "payment-fail";

    }

 
    // ============================================================================

    // BOOKING CREATION → PAYMENT → SUCCESS

    // ============================================================================
 
    @PostMapping("/confirm/{showId}")

    public String confirmBooking(@PathVariable Long showId,

                                 @RequestParam double amount,

                                 Principal principal,

                                 RedirectAttributes ra) {
 
        if (principal == null) {

            return "redirect:/login?error=unauthorized";

        }
 
        Optional<UserDto> userOpt = userService.findByUsername(principal.getName());

        if (userOpt.isEmpty()) userOpt = userService.findByEmail(principal.getName());

        if (userOpt.isEmpty()) return "redirect:/login?error=unauthorized";
 
        UserDto user = userOpt.get();
 
        ShowDto show = showService.getShowById(showId);

        if (show == null) {

            ra.addFlashAttribute("error", "Show not found");

            return "redirect:/";

        }
 
        BookingDto booking = bookingService.createBooking(

                user.getUserId(),

                show.getShowId(),

                amount

        );
 
        // Go to payment with bookingId; payment page shows booking details

        return "redirect:/payment?bookingId=" + booking.getBookingId();

    }
 
    @GetMapping("/success/{bookingId}")

    public String dtoSuccess(@PathVariable Long bookingId, Model model) {

        BookingDto booking = bookingService.getBookingById(bookingId);

        if (booking == null) {

            return "redirect:/history?error=bookingNotFound";

        }

        model.addAttribute("booking", booking);

        return "success";

    }
 
    @GetMapping("/fail/{bookingId}")

    public String dtoFail(@PathVariable Long bookingId, Model model) {

        BookingDto booking = bookingService.failPayment(bookingId);

        model.addAttribute("booking", booking);

        return "payment-fail";

    }
 
    @PostMapping("/cancel/{bookingId}")

    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {

        if (principal == null) {

            return "redirect:/login?error=unauthorized";

        }

        bookingService.cancelBooking(bookingId, principal.getName());

        return "redirect:/history";

    }

 
    // ============================================================================

    // AUX PAGES

    // ============================================================================
 
    @GetMapping("/booking/cancel")

    public String basicCancel() {

        return "payment-fail";

    }
 
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("movies", movieService.getAllMovies());
        return "home";
    }
     
 
    

    @GetMapping("/seat-selection")

    public String seatSelection() {

        return "seat-selection";

    }

 
    // ============================================================================

    // BOOKING LISTS

    // ============================================================================
 
    @GetMapping("/current")

    public String current(Model model, Principal principal) {
 
        if (principal == null) {

            return "redirect:/login?error=unauthorized";

        }
 
        Optional<UserDto> userOpt = userService.findByUsername(principal.getName());

        if (userOpt.isEmpty()) userOpt = userService.findByEmail(principal.getName());

        if (userOpt.isEmpty()) return "redirect:/login?error=unauthorized";
 
        UserDto user = userOpt.get();

        List<BookingDto> bookings = bookingService.getUserBookings(user.getUserId());
 
        List<BookingDto> current = bookings.stream()

                .filter(b -> "PAID".equalsIgnoreCase(b.getPaymentStatus())

                          || "CONFIRMED".equalsIgnoreCase(b.getBookingStatus()))

                .sorted(Comparator.comparing(

                        BookingDto::getBookingDate,

                        Comparator.nullsLast(LocalDate::compareTo)

                ).reversed())

                .toList();
 
        model.addAttribute("bookings", current);

        return "bookings-current";

    }
 
    @GetMapping("/history")

    public String history(Model model, Principal principal) {
 
        if (principal == null) {

            return "redirect:/login?error=unauthorized";

        }
 
        Optional<UserDto> userOpt = userService.findByUsername(principal.getName());

        if (userOpt.isEmpty()) userOpt = userService.findByEmail(principal.getName());

        if (userOpt.isEmpty()) return "redirect:/login?error=unauthorized";
 
        UserDto user = userOpt.get();

        List<BookingDto> bookings = bookingService.getUserBookings(user.getUserId());
 
        List<BookingDto> history = bookings.stream()

                .filter(b -> !"PAID".equalsIgnoreCase(b.getPaymentStatus()))

                .sorted(Comparator.comparing(

                        BookingDto::getBookingDate,

                        Comparator.nullsLast(LocalDate::compareTo)

                ).reversed())

                .toList();
 
        model.addAttribute("bookings", history);

        return "bookings-history";

    }
 
    // ============================================================================

    // Ticket view (renders Ticket.html)

    // ============================================================================
 
// Ticket view (renders Ticket.html)

    @GetMapping("/ticket/{bookingId}")

    public String viewTicket(@PathVariable Long bookingId, Model model) {

        BookingDto booking = bookingService.getBookingById(bookingId);

        if (booking == null) {

            return "redirect:/history?error=bookingNotFound";

        }
 
        // Fetch the show by showId from the booking DTO (adjust getter name if different)

        ShowDto show = null;

        try {

            Long showId = booking.getShowId(); // <-- BookingDto should expose this

            if (showId != null) {

                show = showService.getShowById(showId);

            }

        } catch (Exception ignored) {

            // swallow - template is guarded with "show != null"

        }
 
        model.addAttribute("booking", booking);

        model.addAttribute("show", show); // <-- add separate 'show' object

        return "Ticket"; // file must be templates/Ticket.html (case-sensitive in some OS)

    }
}