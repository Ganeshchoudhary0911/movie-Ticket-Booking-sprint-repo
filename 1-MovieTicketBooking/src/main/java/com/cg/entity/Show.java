package com.cg.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "shows")
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long showId;

	@Column(nullable = false)
	private LocalDate showDate;

	@Column(nullable = false)
	private LocalTime showTime;

	@Column(nullable = false)
	private double price;

	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "theatre_id", nullable = false)
	private Theatre theatre;
	
	@OneToMany(mappedBy = "show", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Seat> seats;

	@OneToMany(mappedBy = "show", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Booking> bookings;
	
	public Show() {
	}
	
// Constructor for Show Test cases.
	public Show(Long showId, LocalDate showDate, LocalTime showTime,
            double price, Movie movie, Theatre theatre) {
    this.showId = showId;
    this.showDate = showDate;
    this.showTime = showTime;
    this.price = price;
    this.movie = movie;
    this.theatre = theatre;
}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

}
