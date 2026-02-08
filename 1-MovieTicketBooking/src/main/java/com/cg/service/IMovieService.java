package com.cg.service;

import java.util.List;

import com.cg.dto.MovieDto;

public interface IMovieService {
    
    List<MovieDto> getAllMovies();

    MovieDto getMovieById(Long id);

    List<MovieDto> searchMovies(String name);

    /**
     * Create or update a movie using a DTO.
     * If dto.movieId is null -> create; otherwise -> update.
     * Returns the saved DTO (with generated id on create).
     */
    MovieDto saveOrUpdateMovie(MovieDto movie);

    void deleteMovie(Long id);
}