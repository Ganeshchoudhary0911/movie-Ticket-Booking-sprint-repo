package com.cg.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class BookingDto {

	private Long bookingId;

	@NotNull(message = "Booking date is required")
	private LocalDate bookingDate;

	@Positive(message = "Total amount must be positive")
	@Digits(integer = 6, fraction = 2, message = "Invalid amount format")
	private double totalAmount;

	private String paymentStatus;
	private String bookingStatus;

	// Reference by ID
	@NotNull(message = "User is required")
	private Long userId;

	@NotNull(message = "Show is required")
	private Long showId;

	// Presentational fields you already use in templates
	private String userUsername;

	// --- NEW: Show/Theatre display fields ---
	private String movieName;
	private String theatreName;
	private LocalDate showDate;
	private LocalTime showTime;

	@NotEmpty(message = "At least one seat must be selected")
	@Pattern(regexp = "^[A-Z]\\d{1,3}$", message = "Seat must be like A1, B12, C120")
	private List<String> seatNumbers;

	public BookingDto() {
	}

	public BookingDto(Long bookingId, LocalDate bookingDate, double totalAmount, String paymentStatus,
			String bookingStatus, Long userId, Long showId, String userUsername, String movieName, String theatreName,
			LocalDate showDate, LocalTime showTime, List<String> seatNumbers) {
		super();
		this.bookingId = bookingId;
		this.bookingDate = bookingDate;
		this.totalAmount = totalAmount;
		this.paymentStatus = paymentStatus;
		this.bookingStatus = bookingStatus;
		this.userId = userId;
		this.showId = showId;
		this.userUsername = userUsername;
		this.movieName = movieName;
		this.theatreName = theatreName;
		this.showDate = showDate;
		this.showTime = showTime;
		this.seatNumbers = seatNumbers;
	}

	// getters/setters
	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getUserUsername() {
		return userUsername;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public LocalDate getShowDate() {
		return showDate;
	}

	public void setShowDate(LocalDate showDate) {
		this.showDate = showDate;
	}

	public LocalTime getShowTime() {
		return showTime;
	}

	public void setShowTime(LocalTime showTime) {
		this.showTime = showTime;
	}

	public List<String> getSeatNumbers() {
		return seatNumbers;
	}

	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
	}

}