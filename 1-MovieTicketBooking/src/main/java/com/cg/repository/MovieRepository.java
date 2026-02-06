package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	    // Custom query to search movies by name
	    List<Movie> findByMovieNameContainingIgnoreCase(String movieName);
	}