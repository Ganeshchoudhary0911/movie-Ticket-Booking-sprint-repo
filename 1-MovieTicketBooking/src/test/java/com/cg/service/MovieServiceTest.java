package com.cg.service;

import com.cg.dto.MovieDto;
import com.cg.entity.Movie;
import com.cg.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;


    // ------------------------------------------------------------
    // POSITIVE TEST : getAllMovies()
    // ------------------------------------------------------------
    @Test
    void testGetAllMovies() {

        Movie m1 = new Movie();
        m1.setMovieId(1L);
        m1.setMovieName("Dune");

        Movie m2 = new Movie();
        m2.setMovieId(2L);
        m2.setMovieName("Avatar");

        when(movieRepository.findAll()).thenReturn(List.of(m1, m2));

        List<MovieDto> dtos = movieService.getAllMovies();

        assertEquals(2, dtos.size());
        assertEquals("Dune", dtos.get(0).getMovieName());
        assertEquals("Avatar", dtos.get(1).getMovieName());
    }


    // ------------------------------------------------------------
    // POSITIVE TEST : getMovieById() found
    // ------------------------------------------------------------
    @Test
    void testGetMovieById_found() {

        Movie m = new Movie();
        m.setMovieId(10L);
        m.setMovieName("Inception");

        when(movieRepository.findById(10L)).thenReturn(Optional.of(m));

        MovieDto dto = movieService.getMovieById(10L);

        assertNotNull(dto);
        assertEquals("Inception", dto.getMovieName());
    }


    // ------------------------------------------------------------
    // NEGATIVE TEST: getMovieById() not found
    // ------------------------------------------------------------
    @Test
    void testGetMovieById_notFound() {

        when(movieRepository.findById(5L)).thenReturn(Optional.empty());

        MovieDto dto = movieService.getMovieById(5L);

        assertNull(dto);
    }


    // ------------------------------------------------------------
    // POSITIVE TEST : searchMovies()
    // ------------------------------------------------------------
    @Test
    void testSearchMovies() {

        Movie m = new Movie();
        m.setMovieId(7L);
        m.setMovieName("Interstellar");

        when(movieRepository.findByMovieNameContainingIgnoreCase("stellar"))
                .thenReturn(List.of(m));

        List<MovieDto> dtos = movieService.searchMovies("stellar");

        assertEquals(1, dtos.size());
        assertEquals("Interstellar", dtos.get(0).getMovieName());
    }


  

   


    // ------------------------------------------------------------
    // POSITIVE TEST : deleteMovie()
    // ------------------------------------------------------------
    @Test
    void testDeleteMovie() {

        doNothing().when(movieRepository).deleteById(50L);

        movieService.deleteMovie(50L);

        verify(movieRepository, times(1)).deleteById(50L);
    }


    // ------------------------------------------------------------
    // POSITIVE TEST : findById() found
    // ------------------------------------------------------------
    @Test
    void testFindById_found() {

        Movie m = new Movie();
        m.setMovieId(55L);
        m.setMovieName("Joker");

        when(movieRepository.findById(55L)).thenReturn(Optional.of(m));

        Optional<MovieDto> dto = movieService.findById(55L);

        assertTrue(dto.isPresent());
        assertEquals("Joker", dto.get().getMovieName());
    }


    // ------------------------------------------------------------
    // NEGATIVE TEST : findById() empty
    // ------------------------------------------------------------
    @Test
    void testFindById_empty() {

        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MovieDto> dto = movieService.findById(99L);

        assertTrue(dto.isEmpty());
    }
}





































