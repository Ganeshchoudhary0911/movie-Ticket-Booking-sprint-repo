package com.cg.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.BookingDto;
import com.cg.entity.Booking;
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.entity.User;
import com.cg.repository.BookingRepository;
import com.cg.repository.SeatRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService implements IBookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private SeatService seatService; // to book seats

	@Autowired
	private SeatLabelResolver seatLabelResolver;

	// ============================================================
	// CREATE BOOKING WITH SEATS (BookMyShow style)
	// ============================================================

	@Transactional
	public BookingDto createBooking(Long userId, Long showId, List<String> seatLabels, double amount) {

		// 1) Resolve seat labels -> seatIds
		List<Long> seatIds = seatLabelResolver.resolveSeatIds(showId, seatLabels);

		// 2) Atomic seat booking (no double booking)
		int updated = seatRepository.markAvailableSeatsAsBooked(showId, seatIds);
		if (updated != seatIds.size()) {
			throw new IllegalStateException(
					"Some seats were just booked by someone else. Please choose different seats.");
		}

		// 3) Create and persist Booking
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

		Show show = showRepository.findById(showId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid showId: " + showId));

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setShow(show);
		booking.setTotalAmount(amount);
		booking.setBookingStatus("CONFIRMED"); // or "PENDING" until payment
		booking.setPaymentStatus("PENDING");
		booking.setBookingDate(LocalDate.now());

		// Attach seats (ManyToMany collection)
		List<Seat> bookedSeats = seatRepository.findAllById(seatIds);
		booking.getSeats().addAll(bookedSeats);

		Booking saved = bookingRepository.save(booking);

		// 4) Return DTO (manual mapping)
		return toDto(saved);
	}

	// ===================== Helper: Entity -> DTO =====================
	private BookingDto toDto(Booking b) {
		BookingDto dto = new BookingDto();

		dto.setBookingId(b.getBookingId());
		dto.setUserId(b.getUser() != null ? b.getUser().getUserId() : null);
		dto.setShowId(b.getShow() != null ? b.getShow().getShowId() : null);

		dto.setTotalAmount(b.getTotalAmount());
		dto.setBookingStatus(b.getBookingStatus());
		dto.setPaymentStatus(b.getPaymentStatus());
		dto.setBookingDate(b.getBookingDate());
		dto.setSeatNumbers(b.getSeats().stream().map(s -> s.getSeatRow() + "-" + s.getSeatNumber()).toList());
		dto.setMovieName(b.getShow().getMovie().getMovieName());
		dto.setTheatreName(b.getShow().getTheatre().getTheatreName());

		// If BookingDto has seat labels or IDs, populate them:
		// Example: set seat names like "A-7"
		if (b.getSeats() != null) {
			List<String> seatNames = b.getSeats().stream().map(s -> s.getSeatRow() + "-" + s.getSeatNumber())
					.collect(Collectors.toList());
			try {
				// If your BookingDto has a setter for seatNames
				java.lang.reflect.Method m = BookingDto.class.getMethod("setSeatNames", List.class);
				m.invoke(dto, seatNames);
			} catch (Exception ignore) {

				// safely ignore if BookingDto doesn't have such field
			}
		}

		// If you also need movie/theatre names in DTO (depends on your BookingDto
		// fields)
		try {
			if (b.getShow() != null && b.getShow().getMovie() != null) {
				var m = BookingDto.class.getMethod("setMovieName", String.class);
				m.invoke(dto, b.getShow().getMovie().getMovieName());
			}
		} catch (Exception ignore) {
		}
		try {
			if (b.getShow() != null && b.getShow().getTheatre() != null) {
				var m = BookingDto.class.getMethod("setTheatreName", String.class);
				m.invoke(dto, b.getShow().getTheatre().getTheatreName());
			}
		} catch (Exception ignore) {
		}

		return dto;
	}

	// ============================================================
	// MAP seatName "A1" → seatId
	// ============================================================
	private List<Long> mapSeatNamesToIds(Long showId, List<String> seatNames) {
		List<Seat> allSeats = seatRepository.findByShowShowId(showId);

		// 1. Define the normalization logic once
		java.util.function.Function<String, String> normalize = seat -> {
			if (seat == null || seat.trim().isEmpty())
				return "";
			String clean = seat.trim().toUpperCase();

			// Extract letters (Row) and digits (Number)
			String row = clean.replaceAll("[^A-Z]", "");
			String digits = clean.replaceAll("[^0-9]", "");

			// Remove leading zeros from digits (e.g., "07" -> "7")
			try {
				if (!digits.isEmpty()) {
					digits = String.valueOf(Integer.parseInt(digits));
				}
			} catch (Exception ignored) {
			}

			return row + digits;
		};

		// 2. Build the DB Map using multiple key strategies for maximum compatibility
		Map<String, Long> dbMap = new HashMap<>();
		for (Seat s : allSeats) {
			String row = (s.getSeatRow() == null ? "" : s.getSeatRow().trim().toUpperCase());
			String num = (s.getSeatNumber() == null ? "" : s.getSeatNumber().trim().toUpperCase());

			// Strategy A: Concatenated Key (e.g., Row "B" + Num "7" = "B7")
			String concatKey = normalize.apply(row + num);
			dbMap.put(concatKey, s.getSeatId());

			// Strategy B: Raw Number Key (e.g., if Num is "B7" and Row is null)
			String rawNumKey = normalize.apply(num);
			dbMap.put(rawNumKey, s.getSeatId());

			// Debugging logs
			System.out.println("Mapped Seat ID " + s.getSeatId() + " to keys: [" + concatKey + ", " + rawNumKey + "]");
		}

		// 3. Match UI seats to DB seats
		List<Long> matchedSeatIds = new ArrayList<>();
		for (String seatName : seatNames) {
			String clientKey = normalize.apply(seatName);

			if (!dbMap.containsKey(clientKey)) {
				// Log what was available to help diagnose missing data
				System.err.println("Failed to match: " + clientKey + ". Available keys: " + dbMap.keySet());
				throw new RuntimeException("Invalid seat: " + seatName);
			}

			matchedSeatIds.add(dbMap.get(clientKey));
		}

		return matchedSeatIds;
	}

	// ============================================================
	// READ METHODS
	// ============================================================
	@Override
	public List<BookingDto> getUserBookings(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return bookingRepository.findByUser(user).stream().map(this::toBookingDto).collect(Collectors.toList());
	}

	@Override
	public BookingDto getBooking(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking != null && booking.getUser() != null && username != null
				&& username.equals(booking.getUser().getUsername())) {
			return toBookingDto(booking);
		}
		return null;
	}

	public BookingDto getBookingById(Long bookingId) {
		return bookingRepository.findById(bookingId).map(this::toBookingDto).orElse(null);
	}

	// ============================================================
	// PAYMENT CONFIRMATION
	// ============================================================
	public BookingDto confirmPayment(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setPaymentStatus("PAID");
		booking.setBookingStatus("CONFIRMED");

		booking = bookingRepository.save(booking);
		return toBookingDto(booking);
	}

	// ============================================================
	// FAIL / CANCEL
	// ============================================================
	@Override
	public BookingDto failPayment(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking != null) {
			booking.setPaymentStatus("FAILED");
			booking.setBookingStatus("CANCELLED");
			booking = bookingRepository.save(booking);
		}
		return toBookingDto(booking);
	}

	@Override
	public BookingDto cancelBooking(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking != null && booking.getUser() != null && username.equals(booking.getUser().getUsername())) {

			booking.setBookingStatus("CANCELLED");
			booking = bookingRepository.save(booking);
		}
		return toBookingDto(booking);
	}

	// ============================================================
	// MAPPING Booking → BookingDto
	// ============================================================
	private BookingDto toBookingDto(Booking b) {
		if (b == null)
			return null;

		BookingDto dto = new BookingDto();
		dto.setBookingId(b.getBookingId());
		dto.setBookingDate(b.getBookingDate());
		dto.setTotalAmount(b.getTotalAmount());
		dto.setPaymentStatus(b.getPaymentStatus());
		dto.setBookingStatus(b.getBookingStatus());

		// User
		if (b.getUser() != null) {
			dto.setUserId(b.getUser().getUserId());
			dto.setUserUsername(b.getUser().getUsername());
		}

		// Show
		if (b.getShow() != null) {
			dto.setShowId(b.getShow().getShowId());
			dto.setShowDate(b.getShow().getShowDate());
			dto.setShowTime(b.getShow().getShowTime());
			dto.setTheatreName(b.getShow().getTheatre().getTheatreName());
			dto.setMovieName(b.getShow().getMovie().getMovieName());
		}

		// ⭐ Seats
		if (b.getSeats() != null) {
			List<String> seatNames = b.getSeats().stream().map(s -> s.getSeatRow() + s.getSeatNumber()).sorted()
					.collect(Collectors.toList());
			dto.setSeatNumbers(seatNames);
		}

		return dto;
	}

	@Transactional
	public void bookSeats(Long showId, List<Long> seatIds) {

		int updated = seatRepository.markAvailableSeatsAsBooked(showId, seatIds);

		if (updated != seatIds.size()) {
			throw new IllegalStateException("Some seats are already booked.");
		}
	}

}