package com.cg.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	private LocalDate bookingDate;
	private double totalAmount;
	private String paymentStatus;
	private String bookingStatus;
	private String seatNumber;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "show_id")
	private Show show;

	public Booking() {
		super();
	}

	// constructor for Booking Test case.
	public Booking(Long bookingId, double totalAmount, String bookingStatus) {
		super();
		this.bookingId = bookingId;
		this.totalAmount = totalAmount;
		this.bookingStatus = bookingStatus;
	}

	public Booking(Long bookingId, LocalDate bookingDate, double totalAmount, String paymentStatus,
			String bookingStatus, User user, Show show, String seatNumber) {
		super();
		this.bookingId = bookingId;
		this.bookingDate = bookingDate;
		this.totalAmount = totalAmount;
		this.paymentStatus = paymentStatus;
		this.bookingStatus = bookingStatus;
		this.user = user;
		this.show = show;
		this.seatNumber = seatNumber;
	}

	// Getter and Setter
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

}
