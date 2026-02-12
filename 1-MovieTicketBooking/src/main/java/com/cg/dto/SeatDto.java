package com.cg.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class SeatDto {

	private Long seatId;

	@NotBlank(message = "Seat number is required")
	@Pattern(regexp = "^[A-Z]\\d{1,3}$", message = "Seat number must be like A10, B5, C120")
	private String seatNumber;

	@NotBlank(message = "Seat row is required")
	@Pattern(regexp = "^[A-Z]$", message = "Seat row must be a single capital letter (e.g., A, B, C)")
	private String seatRow;

	@NotBlank(message = "Seat type is required")
	private String seatType;

	@Positive(message = "Seat price must be positive")
	@Digits(integer = 4, fraction = 2, message = "Invalid seat price format")
	private double seatPrice;

	private boolean booked;

	// Replace the Show entity with just the ID
	private Long showId;

	public SeatDto() {

	}

	public SeatDto(Long seatId, String seatNumber, String seatRow, String seatType, double seatPrice, boolean booked,
			Long showId) {
		super();
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.seatRow = seatRow;
		this.seatType = seatType;
		this.seatPrice = seatPrice;
		this.booked = booked;
		this.showId = showId;
	}

	public Long getSeatId() {
		return seatId;
	}

	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

}
