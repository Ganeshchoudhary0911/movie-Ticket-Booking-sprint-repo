package com.cg.service;

import java.util.List;
import com.cg.dto.BookingDto;

public interface IBookingService {

    BookingDto createBooking(Long userId, Long showId, double amount);

    BookingDto confirmPayment(Long bookingId);

    BookingDto failPayment(Long bookingId);

    BookingDto cancelBooking(Long bookingId, String username);

    BookingDto getBooking(Long bookingId, String username);

    List<BookingDto> getUserBookings(Long userId);

    BookingDto getBookingById(Long bookingId);
}