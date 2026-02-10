package com.cg.booking;

import com.cg.dto.BookingDto;
import com.cg.entity.*;
import com.cg.repository.*;
import com.cg.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock 
    BookingRepository bookingRepository;
    
    @Mock 
    UserRepository userRepository;
    
    @Mock 
    ShowRepository showRepository;

    @InjectMocks 
    BookingService bookingService;

    // Helper method to create a mock User
    private User user() {
        User u = new User();
        u.setUserId(1L);
        u.setUsername("anshu");
        return u;
    }

    // Helper method to create a mock Show
    private Show show() {
        Show s = new Show();
        s.setShowId(10L);
        return s;
    }

    // ================= CONFIRM PAYMENT =================
    @Test
    void confirmPayment_success() {
        Booking b = new Booking();
        b.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenReturn(b);

        BookingDto dto = bookingService.confirmPayment(1L);

        // Verify status changes to PAID and CONFIRMED
        assertEquals("PAID", dto.getPaymentStatus());
        assertEquals("CONFIRMED", dto.getBookingStatus());
    }

    // ================= FAIL PAYMENT =================
    @Test
    void failPayment_success() {
        Booking b = new Booking();
        b.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenReturn(b);

        BookingDto dto = bookingService.failPayment(1L);

        // Verify status changes to FAILED and CANCELLED
        assertEquals("FAILED", dto.getPaymentStatus());
        assertEquals("CANCELLED", dto.getBookingStatus());
    }

    // ================= CANCEL BOOKING =================
    @Test
    void cancelBooking_usernameMatch() {
        User u = user();
        Booking b = new Booking();
        b.setUser(u);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenReturn(b);

        // Attempting cancellation with matching username
        BookingDto dto = bookingService.cancelBooking(1L, "anshu");

        assertEquals("CANCELLED", dto.getBookingStatus());
    }
}
