package com.cg.service;

import com.cg.dto.PaymentDto;
import com.cg.entity.Booking;
import com.cg.entity.Payment;
import com.cg.repository.BookingRepository;
import com.cg.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BookingRepository bookingRepository;

    @InjectMocks private PaymentService paymentService;

    

    // --------------- POSITIVE : existing payment is updated -------------------
    @Test
    void chooseMethodAndPay_updatesExistingPayment() {
        Booking booking = new Booking();
        booking.setBookingId(2L);
        booking.setTotalAmount(300.0);

        Payment existing = new Payment();
        existing.setPaymentId(20L);
        existing.setBooking(booking);
        existing.setAmount(300.0);

        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBooking_BookingId(2L)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);
        when(bookingRepository.save(booking)).thenReturn(booking);

        PaymentDto dto = paymentService.chooseMethodAndPay(2L, "UPI");

        assertNotNull(dto);
        assertEquals(20L, dto.getPaymentId());
        assertEquals("UPI", dto.getMethod());
        assertEquals("PAID", dto.getStatus());
    }

    // -------- POSITIVE : method is set on DTO (new payment scenario) ----------
    @Test
    void chooseMethodAndPay_setsMethodOnDto() {
        Booking booking = new Booking();
        booking.setBookingId(3L);
        booking.setTotalAmount(100.0);

        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBooking_BookingId(3L)).thenReturn(Optional.empty());

        Payment saved = new Payment();
        saved.setPaymentId(30L);
        saved.setBooking(booking);
        saved.setAmount(100.0);
        saved.setMethod("WALLET");
        saved.setStatus("PAID");

        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        PaymentDto dto = paymentService.chooseMethodAndPay(3L, "WALLET");

        assertEquals("WALLET", dto.getMethod());
    }

    // -------------------------- NEGATIVE : booking not found ------------------
    @Test
    void chooseMethodAndPay_bookingNotFound_throws() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.chooseMethodAndPay(999L, "CARD")
        );

        assertEquals("Booking not found: 999", ex.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    // ---------- NEGATIVE : null method (your code allows it; assert null) -----
    @Test
    void chooseMethodAndPay_nullMethod_isAccepted() {
        Booking booking = new Booking();
        booking.setBookingId(4L);
        booking.setTotalAmount(200.0);

        when(bookingRepository.findById(4L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBooking_BookingId(4L)).thenReturn(Optional.empty());

        Payment saved = new Payment();
        saved.setPaymentId(40L);
        saved.setBooking(booking);
        saved.setAmount(200.0);
        saved.setMethod(null);
        saved.setStatus("PAID");

        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        PaymentDto dto = paymentService.chooseMethodAndPay(4L, null);

        assertNull(dto.getMethod());
    }

    // ---- NEGATIVE : save() returns null (edge case) – still returns DTO ------
    @Test
    void chooseMethodAndPay_paymentSaveReturnsNull_stillReturnsDto() {
        Booking booking = new Booking();
        booking.setBookingId(5L);
        booking.setTotalAmount(150.0);

        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBooking_BookingId(5L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(null);
        when(bookingRepository.save(booking)).thenReturn(booking);

        PaymentDto dto = paymentService.chooseMethodAndPay(5L, "CARD");

        assertNotNull(dto);
        assertEquals(5L, dto.getBookingId());
    }
}