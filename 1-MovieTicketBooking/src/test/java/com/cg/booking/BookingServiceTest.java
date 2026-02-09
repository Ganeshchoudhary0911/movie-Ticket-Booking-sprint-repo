package com.cg.booking;

import com.cg.dto.BookingDto;
import com.cg.entity.*;
import com.cg.repository.*;
import com.cg.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock BookingRepository bookingRepository;
    @Mock UserRepository userRepository;
    @Mock ShowRepository showRepository;

    @InjectMocks BookingService bookingService;

    private User user() {
        User u = new User();
        u.setUserId(1L);
        u.setUsername("anshu");
        return u;
    }

    private Show show() {
        Show s = new Show();
        s.setShowId(10L);
        return s;
    }

    // ================= CREATE =================
    @Test
    void createBooking_success() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(showRepository.findById(10L)).thenReturn(Optional.of(show()));
        when(bookingRepository.save(any())).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setBookingId(100L);
            return b;
        });

        BookingDto dto = bookingService.createBooking(1L, 10L, 500);

        assertEquals(100L, dto.getBookingId());
        assertEquals("PAID", dto.getPaymentStatus());
        assertEquals("CONFIRMED", dto.getBookingStatus());
    }

    // ================= GET USER BOOKINGS =================
    @Test
    void getUserBookings_success() {

        User u = user();

        Booking b = new Booking();
        b.setBookingId(1L);
        b.setUser(u);
        b.setShow(show());

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(bookingRepository.findByUser(u)).thenReturn(List.of(b));

        List<BookingDto> list = bookingService.getUserBookings(1L);

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getBookingId());
    }

    // ================= CONFIRM PAYMENT =================
    @Test
    void confirmPayment_success() {

        Booking b = new Booking();
        b.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenReturn(b);

        BookingDto dto = bookingService.confirmPayment(1L);

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

        BookingDto dto = bookingService.cancelBooking(1L, "anshu");

        assertEquals("CANCELLED", dto.getBookingStatus());
    }
}
