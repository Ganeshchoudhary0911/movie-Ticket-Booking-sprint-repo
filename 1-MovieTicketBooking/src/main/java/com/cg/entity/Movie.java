package com.cg.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table
public class Movie {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long movieId;
	 
	    private String movieName;
	    private String genre;
	    private String language;
	    private int duration;
	    private String description;
	    private double rating;
	 
	    @OneToMany(mappedBy = "movie")
	    private List<Show> shows;
	    
	    public Movie() {
	    	
	    }
	    
	    
	    
		public Movie(Long movieId, String movieName, String genre, String language, int duration, String description,
				double rating, List<Show> shows) {
			super();
			this.movieId = movieId;
			this.movieName = movieName;
			this.genre = genre;
			this.language = language;
			this.duration = duration;
			this.description = description;
			this.rating = rating;
			this.shows = shows;
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

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public List<Show> getShows() {
			return shows;
		}

		public void setShows(List<Show> shows) {
			this.shows = shows;
		}
	    
	    

}
