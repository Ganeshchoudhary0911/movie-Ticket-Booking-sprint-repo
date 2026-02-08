package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.BookingDto;
import com.cg.entity.Booking;
import com.cg.entity.Show;
import com.cg.entity.User;
import com.cg.repository.BookingRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.UserRepository;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // ============== CREATE ==============
    @Override
    public BookingDto createBooking(Long userId, Long showId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found: " + showId));

        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setTotalAmount(amount);
        booking.setPaymentStatus("PAID");
        booking.setBookingStatus("CONFIRMED");
        booking.setUser(user);
        booking.setShow(show);

        return toDto(bookingRepository.save(booking));
    }

    // ============== READ BY USER ==============
    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return bookingRepository.findByUser(user).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============== READ BY ID ==============
    @Override
    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        return toDto(booking);
    }

    // ============== CONFIRM PAYMENT ==============
    @Override
    public BookingDto confirmPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setPaymentStatus("PAID");
            booking.setBookingStatus("CONFIRMED");
            return toDto(bookingRepository.save(booking));
        }
        return null;
    }

    // ============== FAIL PAYMENT ==============
    @Override
    public BookingDto failPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setPaymentStatus("FAILED");
            booking.setBookingStatus("CANCELLED");
            return toDto(bookingRepository.save(booking));
        }
        return null;
    }

    // ============== CANCEL BOOKING ==============
    @Override
    public BookingDto cancelBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && booking.getUser() != null &&
            username != null && username.equals(booking.getUser().getUsername())) {
            booking.setBookingStatus("CANCELLED");
            return toDto(bookingRepository.save(booking));
        }
        return null;
    }

    // ============== GET BOOKING (WITH USERNAME CHECK) ==============
    @Override
    public BookingDto getBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && booking.getUser() != null &&
            username != null && username.equals(booking.getUser().getUsername())) {
            return toDto(booking);
        }
        return null;
    }

    // ===== Inline mapping: Entity -> DTO =====
    private BookingDto toDto(Booking b) {
        if (b == null) return null;
        BookingDto dto = new BookingDto();
        dto.setBookingId(b.getBookingId());
        dto.setBookingDate(b.getBookingDate());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setPaymentStatus(b.getPaymentStatus());
        dto.setBookingStatus(b.getBookingStatus());
        dto.setUserId(b.getUser() != null ? b.getUser().getUserId() : null);
        dto.setShowId(b.getShow() != null ? b.getShow().getShowId() : null);

        // 🔹 Populate username for controller checks
        dto.setUserUsername(b.getUser() != null ? b.getUser().getUsername() : null);

        return dto;
    }
}