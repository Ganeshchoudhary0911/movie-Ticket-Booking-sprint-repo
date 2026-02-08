package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Movie;
import com.cg.repository.MovieReposistory;
@Service
public class MovieService implements IMovieService {
	@Autowired
	 MovieReposistory moviereposistory;
	
	 public List<Movie> getAllMovies(){
		 return moviereposistory.findAll(); }
	    public Movie getMovieById(Long id){
	    	return moviereposistory.findById(id).orElse(null); }
	    public List<Movie> searchMovies(String name) { 
	    	return moviereposistory.findByMovieNameContainingIgnoreCase(name); }
	    public void saveOrUpdateMovie(Movie movie) { 
	    	moviereposistory.save(movie); }
	    public void deleteMovie(Long id) { 
	    	moviereposistory.deleteById(id); }
		public Optional<Movie> findById(Long id) {
			return moviereposistory.findById(id);
		}
	

}