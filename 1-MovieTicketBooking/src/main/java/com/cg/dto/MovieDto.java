package com.cg.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovieDto {

	private Long movieId;

	@NotBlank(message = "Movie name is required")
	@Size(min = 2, max = 150, message = "Movie name must be between {min} and {max} characters")
	private String movieName;

	@NotBlank(message = "Genre is required")
	@Size(min = 3, max = 50, message = "Genre must be between {min} and {max} characters")

	private String genre;

	@NotBlank(message = "Language is required")
	@Size(min = 2, max = 30, message = "Language must be between {min} and {max} characters")

	private String language;

	@NotNull(message = "Duration is required")
	@Min(value = 30, message = "Duration must be at least {value} minutes")
	@Max(value = 360, message = "Duration cannot exceed {value} minutes")
	private int duration; // in minutes
	private String description; // long text allowed

	@DecimalMin(value = "0.0", inclusive = true, message = "Rating cannot be negative")
	@DecimalMax(value = "10.0", inclusive = true, message = "Rating cannot exceed {value}")
	private double rating; // <-- wrapper type (nullable)

	@Size(max = 512, message = "Poster URL cannot exceed {max} characters")
	@URL(message = "Poster URL must be a valid URL (http/https)")
	private String posterUrl;

	public MovieDto() {
	}

	public MovieDto(Long movieId, String movieName, String genre, String language, int duration, String description,
			Double rating, String posterUrl) {
		this.movieId = movieId;
		this.movieName = movieName;
		this.genre = genre;
		this.language = language;
		this.duration = duration;
		this.description = description;
		this.rating = rating;
		this.posterUrl = posterUrl;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRating() {   // <-- returns Double (NOT double)
	    return rating;            // <-- NO unboxing, NO defaulting here
	}

	public void setRating(double rating) {  // <-- accepts Double (NOT double)
	    this.rating = rating;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

}
