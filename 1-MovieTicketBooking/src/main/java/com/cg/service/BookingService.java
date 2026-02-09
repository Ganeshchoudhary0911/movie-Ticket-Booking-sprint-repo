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
//    @Override
//    public BookingDto createBooking(Long userId, Long showId, double amount) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
//        Show show = showRepository.findById(showId)
//                .orElseThrow(() -> new RuntimeException("Show not found: " + showId));
//
//        Booking booking = new Booking();
//        booking.setBookingDate(LocalDate.now());
//        booking.setTotalAmount(amount);
//        booking.setPaymentStatus("PENDING");
//        booking.setBookingStatus("CREATED");
//        booking.setUser(user);
//        booking.setShow(show);
//
//        return toDto(bookingRepository.save(booking));
//    }

    // ============== READ BY USER ==============
    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return bookingRepository.findByUser(user).stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }

    // ============== READ BY ID ==============
//    @Override
//    public BookingDto getBookingById(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId).orElse(null);
//        return toDto(booking);
//    }

    // ============== CONFIRM PAYMENT ==============
//    @Override
//    public BookingDto confirmPayment(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId).orElse(null);
//        if (booking != null) {
//            booking.setPaymentStatus("PAID");
//            booking.setBookingStatus("CONFIRMED");
//            return toDto(bookingRepository.save(booking));
//        }
//        return null;
//    }

    // ============== FAIL PAYMENT ==============
    @Override
    public BookingDto failPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setPaymentStatus("FAILED");
            booking.setBookingStatus("CANCELLED");
            return toBookingDto(bookingRepository.save(booking));
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
            return toBookingDto(bookingRepository.save(booking));
        }
        return null;
    }

    // ============== GET BOOKING (WITH USERNAME CHECK) ==============
    @Override
    public BookingDto getBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && booking.getUser() != null &&
            username != null && username.equals(booking.getUser().getUsername())) {
            return toBookingDto(booking);
        }
        return null;
    }

    // ===== Inline mapping: Entity -> DTO =====
 // In BookingService (or wherever you build the DTO)
    private BookingDto toBookingDto(com.cg.entity.Booking b) {
        if (b == null) return null;
        BookingDto dto = new BookingDto();
        dto.setBookingId(b.getBookingId());
        dto.setBookingDate(b.getBookingDate());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setPaymentStatus(b.getPaymentStatus());
        dto.setBookingStatus(b.getBookingStatus());

        if (b.getUser() != null) {
            dto.setUserId(b.getUser().getUserId());
            dto.setUserUsername(b.getUser().getUsername()); // adjust getter if needed
        }
        if (b.getShow() != null) {
            dto.setShowId(b.getShow().getShowId());
            dto.setShowDate(b.getShow().getShowDate());
            dto.setShowTime(b.getShow().getShowTime());
            dto.setShowPrice(b.getShow().getPrice());

            if (b.getShow().getMovie() != null) {
                dto.setMovieName(b.getShow().getMovie().getMovieName()); // adjust getter name if different
            }
            if (b.getShow().getTheatre() != null) {
                dto.setTheatreName(b.getShow().getTheatre().getTheatreName()); // adjust if different
            }
        }
        return dto;
    }

    public BookingDto getBookingById(Long bookingId) {
        var b = bookingRepository.findById(bookingId).orElse(null);
        return toBookingDto(b);
    }

    public BookingDto createBooking(Long userId, Long showId, double amount) {
        var b = new com.cg.entity.Booking();
        b.setUser(userRepository.getReferenceById(userId));
        b.setShow(showRepository.getReferenceById(showId));
        b.setTotalAmount(amount);
        b.setBookingDate(java.time.LocalDate.now());

        // Ensure PENDING (so your payment page shows PENDING before Pay Now)
        b.setPaymentStatus("PENDING");
        b.setBookingStatus("CREATED");

        b = bookingRepository.save(b);
        return toBookingDto(b);
    }

    public BookingDto confirmPayment(Long bookingId) {
        var b = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
        b.setPaymentStatus("PAID");
        b.setBookingStatus("CONFIRMED");
        b = bookingRepository.save(b);
        return toBookingDto(b);
    }
}