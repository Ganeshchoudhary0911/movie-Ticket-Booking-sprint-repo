package com.cg.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.PaymentDto;
import com.cg.entity.Booking;
import com.cg.entity.Payment;
import com.cg.repository.BookingRepository;
import com.cg.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentDto chooseMethodAndPay(Long bookingId, String method) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        Payment payment = paymentRepository.findByBooking_BookingId(bookingId).orElse(null);
        if (payment == null) {
            payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalAmount());
            payment.setCreatedAt(LocalDateTime.now());
        }
        payment.setMethod(method);
        payment.setStatus("PAID");
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        booking.setPaymentStatus("PAID");
        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);

        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setBookingId(bookingId);
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setGatewayRef(payment.getGatewayRef());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}