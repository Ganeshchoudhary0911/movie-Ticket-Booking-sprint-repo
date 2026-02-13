package com.cg.service;

import com.cg.dto.BookingDto;
import com.cg.entity.*;
import com.cg.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ShowRepository showRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private SeatService seatService; // not used directly by methods, but present in service
    @Mock private SeatLabelResolver seatLabelResolver;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Show show;
    private Movie movie;
    private Theatre theatre;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("ganesh");

        movie = new Movie();
        movie.setMovieName("Dune 2");

        theatre = new Theatre();
        theatre.setTheatreName("PVR Hyderabad");

        show = new Show();
        show.setShowId(10L);
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(LocalDate.of(2026, 2, 12));
        show.setShowTime(LocalTime.of(18, 30));
    }

    

    // ------------------------------------------------------------
    //  getUserBookings(...) — POSITIVE
    // ------------------------------------------------------------
    @Test
    void getUserBookings_returnsDtos() {
        // repo user lookup
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking b1 = new Booking();
        b1.setBookingId(1L);
        b1.setUser(user);
        b1.setShow(show);
        b1.setTotalAmount(300.0);
        b1.setPaymentStatus("PAID");
        b1.setBookingStatus("CONFIRMED");
        b1.setBookingDate(LocalDate.now());
        b1.getSeats().add(buildSeat(201L, "B", "5"));

        Booking b2 = new Booking();
        b2.setBookingId(2L);
        b2.setUser(user);
        b2.setShow(show);
        b2.setTotalAmount(500.0);
        b2.setPaymentStatus("PENDING");
        b2.setBookingStatus("CONFIRMED");
        b2.setBookingDate(LocalDate.now());
        b2.getSeats().add(buildSeat(202L, "C", "10"));

        when(bookingRepository.findByUser(user)).thenReturn(List.of(b1, b2));

        List<BookingDto> dtos = bookingService.getUserBookings(1L);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getBookingId()).isEqualTo(1L);
        assertThat(dtos.get(1).getBookingId()).isEqualTo(2L);
        assertThat(dtos.get(0).getMovieName()).isEqualTo("Dune 2");
        assertThat(dtos.get(1).getTheatreName()).isEqualTo("PVR Hyderabad");
    }

    // ------------------------------------------------------------
    //  getBooking(...) — POSITIVE (username matches)
    // ------------------------------------------------------------
    @Test
    void getBooking_returnsDto_whenUsernameMatches() {
        Booking b = new Booking();
        b.setBookingId(55L);
        b.setUser(user);
        b.setShow(show);
        b.getSeats().add(buildSeat(300L, "D", "1"));

        when(bookingRepository.findById(55L)).thenReturn(Optional.of(b));

        BookingDto dto = bookingService.getBooking(55L, "ganesh");

        assertNotNull(dto);
        assertEquals(55L, dto.getBookingId());
        assertEquals("ganesh", dto.getUserUsername());
    }

    // ------------------------------------------------------------
    //  getBookingById(...) — NEGATIVE (not found -> null)
    // ------------------------------------------------------------
    @Test
    void getBookingById_returnsNull_whenNotFound() {
        when(bookingRepository.findById(123L)).thenReturn(Optional.empty());
        BookingDto dto = bookingService.getBookingById(123L);
        assertNull(dto);
    }

    // ------------------------------------------------------------
    //  confirmPayment(...) — POSITIVE
    // ------------------------------------------------------------
    @Test
    void confirmPayment_setsPaidAndConfirmed() {
        Booking existing = new Booking();
        existing.setBookingId(70L);
        existing.setUser(user);
        existing.setShow(show);
        existing.setPaymentStatus("PENDING");
        existing.setBookingStatus("PENDING");

        when(bookingRepository.findById(70L)).thenReturn(Optional.of(existing));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        BookingDto dto = bookingService.confirmPayment(70L);

        assertEquals("PAID", dto.getPaymentStatus());
        assertEquals("CONFIRMED", dto.getBookingStatus());
        verify(bookingRepository).save(argThat(b ->
                "PAID".equals(b.getPaymentStatus()) && "CONFIRMED".equals(b.getBookingStatus())
        ));
    }

    // ------------------------------------------------------------
    // 6) failPayment(...) — NEGATIVE (not found -> null)
    // ------------------------------------------------------------
    @Test
    void failPayment_returnsNull_whenBookingNotFound() {
        when(bookingRepository.findById(333L)).thenReturn(Optional.empty());
        BookingDto dto = bookingService.failPayment(333L);
        assertNull(dto);
    }

   

    // ------------------------------------------------------------
    //  bookSeats(...) — NEGATIVE (seat conflict -> throws)
    // ------------------------------------------------------------
    @Test
    void bookSeats_throws_whenAnySeatAlreadyBooked() {
        Long showId = 10L;
        List<Long> seatIds = List.of(1L, 2L, 3L);

        // returns less updates than expected -> conflict
        when(seatRepository.markAvailableSeatsAsBooked(showId, seatIds)).thenReturn(2);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> bookingService.bookSeats(showId, seatIds));

        assertEquals("Some seats are already booked.", ex.getMessage());
    }

    // ---------- helpers ----------

    private Seat buildSeat(Long id, String row, String num) {
        Seat s = new Seat();
        s.setSeatId(id);
        s.setSeatRow(row);
        s.setSeatNumber(num);
        return s;
    }
}






































