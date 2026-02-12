package com.cg.service;

import com.cg.dto.MovieDto;
import com.cg.entity.Movie;
import com.cg.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * MovieService Tests (5 test methods)
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("getAllMovies - Returns list of all movies")
    void shouldGetAllMovies() {
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setMovieName("Avatar");

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDto> result = movieService.getAllMovies();

        assertEquals(1, result.size());
        assertEquals("Avatar", result.get(0).getMovieName());
        verify(movieRepository).findAll();
    }

    @Test
    @DisplayName("getAllMovies - Returns empty list when no movies")
    void shouldGetAllMovies_returnEmpty() {
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        List<MovieDto> result = movieService.getAllMovies();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getMovieById - Returns movie when found")
    void shouldGetMovieById_whenFound() {
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setMovieName("Avatar");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieDto result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getMovieId());
    }

    @Test
    @DisplayName("getMovieById - Returns null when not found")
    void shouldGetMovieById_returnNull() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        MovieDto result = movieService.getMovieById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("deleteMovie - Deletes movie by ID")
    void shouldDeleteMovie() {
        movieService.deleteMovie(1L);

        verify(movieRepository).deleteById(1L);
    }
}