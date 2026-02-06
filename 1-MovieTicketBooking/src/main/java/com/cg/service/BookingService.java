package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.*;
import com.cg.repository.BookingRepository;

@Service
public class BookingService implements IBookingService {
	
	@Autowired
	private BookingRepository bookingRepository;

	public BookingService(BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;
	}

	@Override
	public Booking createBooking(User user, Show show, double amount) {

		Booking booking = new Booking();
		booking.setBookingDate(LocalDate.now());
		booking.setTotalAmount(amount);
		booking.setPaymentStatus("PAID");
		booking.setBookingStatus("CONFIRMED");
		booking.setUser(user);
		booking.setShow(show);

		return bookingRepository.save(booking);
	}

	public List<Booking> getUserBookings(Optional<User> user) {
		return bookingRepository.findByUser(user);
	}

	@Override
	public Booking getBookingById(Long bookingId) {
		return bookingRepository.findById(bookingId).orElse(null);
	}

	@Override
	public Booking confirmPayment(Long bookingId) {

		Booking booking = getBookingById(bookingId);
		if (booking != null) {
			booking.setPaymentStatus("PAID");
			booking.setBookingStatus("CONFIRMED");
			return bookingRepository.save(booking);
		}
		return null;
	}

	@Override
	public Booking failPayment(Long bookingId) {

		Booking booking = getBookingById(bookingId);
		if (booking != null) {
			booking.setPaymentStatus("FAILED");
			booking.setBookingStatus("CANCELLED");
			return bookingRepository.save(booking);
		}
		return null;
	}

	@Override
	public Booking cancelBooking(Long bookingId, String username) {

		Booking booking = getBookingById(bookingId);
		if (booking != null && booking.getUser().getUsername().equals(username)) {
			booking.setBookingStatus("CANCELLED");
			return bookingRepository.save(booking);
		}
		return null;
	}

	@Override
	public Booking getBooking(Long bookingId, String username) {

		Booking booking = getBookingById(bookingId);
		if (booking != null && booking.getUser().getUsername().equals(username)) {
			return booking;
		}
		return null;

	}

	@Override
	public List<Booking> getUserBookings(User user) {
		return bookingRepository.findByUser(user);
	}
}
