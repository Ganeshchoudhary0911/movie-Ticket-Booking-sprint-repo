package com.cg;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cg.entity.Movie;
import com.cg.service.MovieService;
@SpringBootTest
public class MovieServiceTest {
	@Autowired
	private MovieService movieService;

	@BeforeAll
	public static void init() {
		System.out.println("Movie Test Cases");
	}

	@Test
	public void testAddMovie() {
		Movie movie = new Movie("Inception", "Sci-Fi");
		Movie saved = movieService.addMovie(movie);

		assertEquals("Inception", saved.getMovieName());
	}

	@Test
	public void testUpdateMovie() {
		Movie movie = new Movie("Avatar", "Sci-Fi");
		movieService.addMovie(movie);

		movie.setGenre("Adventure");
		Movie updated = movieService.saveOrUpdateMovie(movie);

		assertEquals("Adventure", updated.getGenre());
	}

	@Test
	public void testDeleteMovie() {
		Movie movie = new Movie("Titanic", "Romance");
		// Save and fetch back to get a generated id
		Movie saved = movieService.saveOrUpdateMovie(movie);

		movieService.deleteMovie(saved.getMovieId());

		Movie after = movieService.getMovieById(saved.getMovieId());
		assertNull(after); // confirms deletion
	}

	@Test
	public void testSearchMovie() {
		movieService.addMovie(new Movie("RRR", "Action"));

		List<Movie> movie = movieService.searchMovies("RRR");
		assertNotNull(movie);
	}
}