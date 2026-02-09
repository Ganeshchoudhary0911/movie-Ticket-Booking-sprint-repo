package com.cg.controller;

import com.cg.dto.BookingDto;
import com.cg.service.BookingService;
import com.cg.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment") // <-- namespace all payment gateway routes
public class PaymentController {

    private final BookingService bookingService;
    private final PaymentService paymentService;

    public PaymentController(BookingService bookingService, PaymentService paymentService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    @GetMapping("/method")
    public String selectMethod(@RequestParam Long bookingId, Model model) {
        BookingDto booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "redirect:/history?error=bookingNotFound";
        model.addAttribute("booking", booking);
        return "payment-method"; // templates/payment-method.html
    }

    @PostMapping("/choose")
    public String chooseAndPay(@RequestParam Long bookingId,
                               @RequestParam String method,
                               RedirectAttributes ra) {
        paymentService.chooseMethodAndPay(bookingId, method);
        ra.addFlashAttribute("paidBy", method);
        return "redirect:/success/" + bookingId;
    }
}