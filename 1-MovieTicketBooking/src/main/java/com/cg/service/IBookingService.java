package com.cg.service;

import java.util.List;
import com.cg.entity.Booking;
import com.cg.entity.Show;
import com.cg.entity.User;

public interface IBookingService {

	public Booking createBooking(User user, Show show, double amount);

	public Booking confirmPayment(Long bookingId);

	public Booking failPayment(Long bookingId);

	public Booking cancelBooking(Long bookingId, String username);

	public Booking getBooking(Long bookingId, String username);

	public List<Booking> getUserBookings(User user);

	public Booking getBookingById(Long bookingId);
}
