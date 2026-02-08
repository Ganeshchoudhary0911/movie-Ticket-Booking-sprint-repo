package com.cg.dto;

import java.time.LocalDate;

public class BookingDto {

	private Long bookingId;
	private LocalDate bookingDate;
	private double totalAmount;
	private String paymentStatus;
	private String bookingStatus;

	// Reference associated entities by ID only
	private Long userId;
	private Long showId;
	
	private String userUsername;
	
	
	public BookingDto(Long bookingId, LocalDate bookingDate, double totalAmount, String paymentStatus,
			String bookingStatus, Long userId, Long showId, String userUsername) {
		super();
		this.bookingId = bookingId;
		this.bookingDate = bookingDate;
		this.totalAmount = totalAmount;
		this.paymentStatus = paymentStatus;
		this.bookingStatus = bookingStatus;
		this.userId = userId;
		this.showId = showId;
		this.userUsername = userUsername;
	}
	public BookingDto() {
		
	}
	public String getUserUsername() {
		return userUsername;
	}
	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}
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

	
}
