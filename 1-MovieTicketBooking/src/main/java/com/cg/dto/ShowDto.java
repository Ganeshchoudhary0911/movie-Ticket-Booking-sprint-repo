package com.cg.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowDto {

	private Long showId;
	private LocalDate showDate;
	private LocalTime showTime;
	private double price;

	// Use IDs for associations to avoid leaking entity graphs
	private Long movieId;
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
