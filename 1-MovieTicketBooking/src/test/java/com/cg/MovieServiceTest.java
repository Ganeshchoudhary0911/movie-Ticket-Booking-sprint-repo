package com.cg; 
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cg.entity.Movie;
import com.cg.service.MovieService;

public class MovieServiceTest {

    static MovieService movieService;

    @BeforeAll
    public static void init() {
        movieService = new MovieService();
    }

    @Test
    public void testAddMovie() {
        Movie m = new Movie("Inception", "Sci-Fi");
        Movie saved = movieService.addMovie(m);

        assertEquals("Inception", saved.getTitle());
    }

    @Test
    public void testUpdateMovie() {
        Movie m = new Movie("Avatar", "Sci-Fi");
        movieService.addMovie(m);

        m.setGenre("Adventure");
        Movie updated = movieService.updateMovie(m);

        assertEquals("Adventure", updated.getGenre());
    }

    @Test
    public void testDeleteMovie() {
        Movie m = new Movie("Titanic", "Romance");
        movieService.addMovie(m);

        boolean result = movieService.deleteMovie(m.getId());
        assertTrue(result);
    }

    @Test
    public void testSearchMovie() {
        movieService.addMovie(new Movie("RRR", "Action"));

        Movie movie = movieService.searchMovie("RRR");
        assertNotNull(movie);
    }
}