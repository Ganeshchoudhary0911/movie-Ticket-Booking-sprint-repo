package com.cg.service;

import com.cg.dto.BookingDto;
import com.cg.entity.*;
import com.cg.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BookingService Tests (3 test methods)
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatService seatService;

    @Mock
    private SeatLabelResolver seatLabelResolver;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("createBooking - Creates booking with valid data")
    void shouldCreateBooking_withValidData() {
        User user = new User();
        user.setUserId(1L);

        Show show = new Show();
        show.setShowId(1L);
        show.setMovie(new Movie());
        show.setTheatre(new Theatre());

        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setUser(user);
        booking.setShow(show);
        booking.setTotalAmount(500.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(seatLabelResolver.resolveSeatIds(1L, List.of("A1"))).thenReturn(List.of(1L));
        when(seatRepository.markAvailableSeatsAsBooked(1L, List.of(1L))).thenReturn(1);
        when(seatRepository.findAllById(List.of(1L))).thenReturn(List.of());
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto result = bookingService.createBooking(1L, 1L, List.of("A1"), 500.0);

        assertNotNull(result);
        assertEquals(1L, result.getBookingId());
        verify(bookingRepository).save(any());
    }

    @Test
    @DisplayName("createBooking - Throws when user not found")
    void shouldCreateBooking_throwWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(99L, 1L, List.of("A1"), 500.0));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBooking - Throws when show not found")
    void shouldCreateBooking_throwWhenShowNotFound() {
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(1L, 99L, List.of("A1"), 500.0));
    }
}