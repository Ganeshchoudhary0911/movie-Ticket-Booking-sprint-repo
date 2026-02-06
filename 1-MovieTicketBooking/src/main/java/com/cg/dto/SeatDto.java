package com.cg.dto;

public class SeatDto {

	private Long seatId;
	private String seatNumber;
	private String seatRow;
	private String seatType; // Regular/Premium
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
