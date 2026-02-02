package com.cg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seatId;
	private String seatNumber;
	private String seatRow;
	private String seatType;  // Regular/Premium
	private double seatPrice;
	private boolean isBooked;

	@ManyToOne
	@JoinColumn(name = "show_id")
	private Show show;

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
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public Seat(Long seatId, String seatNumber, String seatRow, String seatType, double seatPrice, boolean isBooked,
			Show show) {
		super();
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.seatRow = seatRow;
		this.seatType = seatType;
		this.seatPrice = seatPrice;
		this.isBooked = isBooked;
		this.show = show;
	}

	public Seat() {
		super();
	}
	
	
	
	

}
