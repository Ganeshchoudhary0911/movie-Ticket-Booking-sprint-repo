package com.cg.service;

import java.util.List;

import com.cg.entity.Movie;

public interface IMovieService {
	
	    public List<Movie> getAllMovies(); 
	    public Movie getMovieById(Long id); 
	    public List<Movie> searchMovies(String name); 
	    public Movie saveOrUpdateMovie(Movie movie); 
	    public void deleteMovie(Long id); 

}