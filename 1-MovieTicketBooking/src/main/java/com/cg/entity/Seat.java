package com.cg.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Index;

@Entity

@Table(
    name = "seat",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_show_row_number", columnNames = {"show_id", "seat_row", "seat_number"})
    },
    indexes = {
        @Index(name = "idx_seat_show", columnList = "show_id"),
        @Index(name = "idx_seat_booked", columnList = "show_id, is_booked")
    }
)

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
	
	@ManyToMany(mappedBy = "seats")
	private Set<Booking> bookings = new HashSet<>();

	public Seat(Long seatId, String seatNumber, String seatRow, String seatType, double seatPrice, boolean isBooked,
			Show show, Set<Booking> bookings) {
		super();
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.seatRow = seatRow;
		this.seatType = seatType;
		this.seatPrice = seatPrice;
		this.isBooked = isBooked;
		this.show = show;
		this.bookings = bookings;
	}

	public Seat() {

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
	
	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
}
