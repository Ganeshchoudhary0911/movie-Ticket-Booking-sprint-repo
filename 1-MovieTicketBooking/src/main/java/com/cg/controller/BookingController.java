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
import com.cg.service.ShowService;
import com.cg.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
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

    // ============================================================================
    // OLD SIMPLE PAYMENT FLOW (seat selection → payment)
    // ============================================================================

    // IMPORTANT: THIS METHOD MUST COME FIRST
    @GetMapping("/payment")
    public String basicPayment(@RequestParam(required = false) Integer seats,
                               @RequestParam(required = false) Integer amount,
                               @RequestParam(required = false) Long bookingId,
                               Model model) {

        // CASE 1: Simple seat-selection flow
        if (bookingId == null && seats != null && amount != null) {
            model.addAttribute("seats", seats);
            model.addAttribute("amount", amount);
            return "payment";
        }

        // CASE 2: DTO flow
        if (bookingId != null) {
            return "forward:/payment/dto?bookingId=" + bookingId;
        }

        // CASE 3: fallback if parameters missing
        return "payment";   // Never return "error" here
    }

    @GetMapping("/booking/success")
    public String basicSuccess(@RequestParam Long bookingId, Model model) {
        BookingDto booking = bookingService.getBookingById(bookingId);
        model.addAttribute("booking", booking);
        return "success";
    }

    @GetMapping("/booking/cancel")
    public String basicCancel() { return "payment-fail"; }

    @GetMapping("/shows")
    public String showsPage() { return "home"; }

    @GetMapping("/tryagain")
    public String tryAgain() { return "seat-selection"; }

    // ============================================================================
    // DTO BOOKING SYSTEM (confirm → payment → success)
    // ============================================================================

    @PostMapping("/confirm/{showId}")
    public String confirmBooking(@PathVariable Long showId,
                                 @RequestParam double amount,
                                 Principal principal,
                                 RedirectAttributes ra) {

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

        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }

    // DTO Payment Page
    @GetMapping("/payment/dto")
    public String dtoPayment(@RequestParam Long bookingId, Model model) {

        BookingDto booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "error";

        model.addAttribute("booking", booking);
        return "payment";
    }
    
    @PostMapping("/payment/confirm")
    public String confirmPayment(@RequestParam Long bookingId,
                                 RedirectAttributes ra) {

        BookingDto updated = bookingService.confirmPayment(bookingId);
        ra.addFlashAttribute("booking", updated);

        return "redirect:/success/" + bookingId;
    }

    @GetMapping("/success/{bookingId}")
    public String dtoSuccess(@PathVariable Long bookingId, Model model) {
        BookingDto booking = bookingService.getBookingById(bookingId);
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
        bookingService.cancelBooking(bookingId, principal.getName());
        return "redirect:/history";
    }

    // ============================================================================
    // BOOKING LISTS
    // ============================================================================

    @GetMapping("/current")
    public String current(Model model, Principal principal) {

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
}









//package com.cg.controller;
//
//import java.security.Principal;
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.cg.dto.BookingDto;
//import com.cg.dto.ShowDto;
//import com.cg.dto.UserDto;
//import com.cg.service.BookingService;
//import com.cg.service.ShowService;
//import com.cg.service.UserService;
//
//@Controller
//@RequestMapping("/bookings")   // CLASS-LEVEL PREFIX
//public class BookingController {
//
//    private final BookingService bookingService;
//    private final ShowService showService;
//    private final UserService userService;
//
//    public BookingController(BookingService bookingService,
//                             ShowService showService,
//                             UserService userService) {
//        this.bookingService = bookingService;
//        this.showService = showService;
//        this.userService = userService;
//    }
//
//    // Create booking → goes to /payment page
//    @PostMapping("/confirm/{showId}")
//    public String confirmBooking(@PathVariable Long showId,
//                                 @RequestParam double amount,
//                                 Principal principal,
//                                 RedirectAttributes ra) {
//
//        Optional<UserDto> optUser = userService.findByUsername(principal.getName());
//        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
//        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";
//
//        UserDto user = optUser.get();
//
//        ShowDto show = showService.getShowById(showId);
//        if (show == null) {
//            ra.addFlashAttribute("error", "Show not found");
//            return "redirect:/";
//        }
//
//        // Create booking using IDs (DTO approach)
//        BookingDto booking = bookingService.createBooking(user.getUserId(), show.getShowId(), amount);
//
//        ra.addFlashAttribute("booking", booking);
//        return "redirect:/payment?bookingId=" + booking.getBookingId();
//    }
//
//
//    @GetMapping("/success/{bookingId}")
//    public String paymentSuccess(@PathVariable Long bookingId, Model model) {
//        BookingDto booking = bookingService.confirmPayment(bookingId);
//        model.addAttribute("booking", booking);
//        return "success";
//    }
//
//    @GetMapping("/fail/{bookingId}")
//    public String paymentFail(@PathVariable Long bookingId, Model model, Principal principal) {
//        BookingDto existing = bookingService.getBookingById(bookingId);
//        if (existing == null) return "error";
//
//        // Compare principal to booking owner (assumes BookingDto has userUsername or similar)
//        if (existing.getUserUsername() == null || !existing.getUserUsername().equals(principal.getName()))
//            return "error";
//
//        BookingDto booking = bookingService.failPayment(bookingId);
//        model.addAttribute("booking", booking);
//        return "payment-fail";
//    }
//
//    @PostMapping("/cancel/{bookingId}")
//    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {
//        String username = principal.getName();
//        BookingDto booking = bookingService.cancelBooking(bookingId, username);
//        if (booking == null) return "error";
//
//        return "redirect:/bookings/history";
//    }
//
//    private static boolean eq(String a, String b) {
//        return a != null && a.equalsIgnoreCase(b);
//    }
//
//    // --------------------------------------------------------
//    // ❗ FIXED: Correct URLs (only method-level paths needed)
//    // --------------------------------------------------------
//
//    @GetMapping("/current")   // final URL = /bookings/current   ✔ CORRECT
//    public String currentBookings(Model model, Principal principal) {
//
//        Optional<UserDto> optUser = userService.findByUsername(principal.getName());
//        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
//        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";
//
//        UserDto user = optUser.get();
//
//        List<BookingDto> bookings = bookingService.getUserBookings(user.getUserId());
//        List<BookingDto> current = bookings.stream()
//                .filter(b -> eq(b.getBookingStatus(), "CONFIRMED") ||
//                             eq(b.getPaymentStatus(), "PAID"))
//                .sorted(Comparator.comparing(
//                        BookingDto::getBookingDate,
//                        Comparator.nullsLast(LocalDate::compareTo)
//                ).reversed())
//                .toList();
//
//        model.addAttribute("bookings", current);
//        return "bookings-current";
//    }
//
//
//    @GetMapping("/history")  // final URL = /bookings/history  ✔ CORRECT
//    public String bookingHistory(Model model, Principal principal) {
//
//        Optional<UserDto> optUser = userService.findByUsername(principal.getName());
//        if (optUser.isEmpty()) optUser = userService.findByEmail(principal.getName());
//        if (optUser.isEmpty()) return "redirect:/login?error=unauthorized";
//
//        UserDto user = optUser.get();
//
//        List<BookingDto> bookings = bookingService.getUserBookings(user.getUserId());
//        List<BookingDto> history = bookings.stream()
//                .filter(b -> !eq(b.getBookingStatus(), "CONFIRMED") &&
//                             !eq(b.getPaymentStatus(), "PAID"))
//                .sorted(Comparator.comparing(
//                        BookingDto::getBookingDate,
//                        Comparator.nullsLast(LocalDate::compareTo)
//                ).reversed())
//                .toList();
//
//        model.addAttribute("bookings", history);
//        return "bookings-history";
//    }
//}