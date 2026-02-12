package com.cg.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ShowDto {

	private Long showId;

	@NotNull(message = "Show date is required")
	@FutureOrPresent(message = "Show date cannot be in the past")
	private LocalDate showDate;

	@NotNull(message = "Show time is required")
	private LocalTime showTime;

	private double price;

	@NotNull(message = "Movie ID is required")
	private Long movieId;

	@NotNull(message = "Theatre ID is required")
	private Long theatreId;

	private String movieName;

	private String theatreName;

	public ShowDto() {
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

	public ShowDto(Long showId, LocalDate showDate, LocalTime showTime, double price, Long movieId, Long theatreId,
			String movieName, String theatreName) {
		super();
		this.showId = showId;
		this.showDate = showDate;
		this.showTime = showTime;
		this.price = price;
		this.movieId = movieId;
		this.theatreId = theatreId;
		this.movieName = movieName;
		this.theatreName = theatreName;
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

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Long theatreId) {
		this.theatreId = theatreId;
	}

}
