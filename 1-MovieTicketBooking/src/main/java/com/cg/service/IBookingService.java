package com.cg.service;

import java.util.List;
import com.cg.entity.Booking;
import com.cg.entity.Show;
import com.cg.entity.User;

public interface IBookingService {

    Booking createBooking(User user, Show show, double amount);

    Booking confirmPayment(Long bookingId);

    Booking failPayment(Long bookingId);

    Booking cancelBooking(Long bookingId, String username);

    Booking getBooking(Long bookingId, String username);

    List<Booking> getUserBookings(User user);

    Booking getBookingById(Long bookingId);
}
