package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.entity.Theatre;

public interface ShowRepository extends JpaRepository<Show, Long> {
	 
    List<Show> findByMovie(Movie movie);
 
    List<Show> findByTheatre(Theatre theatre);
    
    List<Show> findByMovieMovieId(Long movieId);
    
    List<Show> findByMovie_MovieIdOrderByShowDateAscShowTimeAsc(Long movieId);
}