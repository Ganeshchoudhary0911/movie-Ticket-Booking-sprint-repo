package com.cg.movie;

import com.cg.dto.MovieDto;
import com.cg.entity.Movie;
import com.cg.repository.MovieRepository;
import com.cg.service.MovieService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

	@Mock
	private MovieRepository movieRepository;

	@InjectMocks
	private MovieService movieService;

	// -------- helper builders --------
	private Movie movie(Long id, String name, String genre, String lang, int duration, String description,
			double rating, String posterUrl) {
		Movie m = new Movie();
		m.setMovieId(id);
		m.setMovieName(name);
		m.setGenre(genre);
		m.setLanguage(lang);
		m.setDuration(duration);
		m.setDescription(description);
		m.setRating(rating);
		m.setPosterUrl(posterUrl);
		return m;
	}

	private MovieDto dto(Long id, String name, String genre, String lang, int duration, String description,
			double rating, String posterUrl) {
		MovieDto d = new MovieDto();
		d.setMovieId(id);
		d.setMovieName(name);
		d.setGenre(genre);
		d.setLanguage(lang);
		d.setDuration(duration);
		d.setDescription(description);
		d.setRating(rating);
		d.setPosterUrl(posterUrl);
		return d;
	}

	// --------------- tests ---------------

	@Test
	@DisplayName("getAllMovies: returns mapped DTO list")
	void getAllMovies_returnsList() {
		Movie m1 = movie(1L, "Inception", "Sci-Fi", "English", 148, "desc1", 8.8, "p1");
		Movie m2 = movie(2L, "Interstellar", "Sci-Fi", "English", 169, "desc2", 8.6, "p2");
		when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

		List<MovieDto> result = movieService.getAllMovies();

		assertEquals(2, result.size());
		assertEquals("Inception", result.get(0).getMovieName());
		assertEquals("Interstellar", result.get(1).getMovieName());
		verify(movieRepository).findAll();
	}

	@Test
	@DisplayName("getAllMovies: empty DB -> returns empty list")
	void getAllMovies_empty() {
		when(movieRepository.findAll()).thenReturn(Collections.emptyList());

		List<MovieDto> result = movieService.getAllMovies();

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(movieRepository).findAll();
	}

	@Test
	@DisplayName("getMovieById: found -> returns mapped DTO")
	void getMovieById_found() {
		Movie m = movie(10L, "Tenet", "Sci-Fi", "English", 150, "Time inversion", 7.5, "poster");
		when(movieRepository.findById(10L)).thenReturn(Optional.of(m));

		MovieDto dto = movieService.getMovieById(10L);

		assertNotNull(dto);
		assertEquals(10L, dto.getMovieId());
		assertEquals("Tenet", dto.getMovieName());
		verify(movieRepository).findById(10L);
	}

	@Test
	@DisplayName("getMovieById: not found -> returns null")
	void getMovieById_notFound() {
		when(movieRepository.findById(99L)).thenReturn(Optional.empty());

		MovieDto dto = movieService.getMovieById(99L);

		assertNull(dto);
		verify(movieRepository).findById(99L);
	}

	@Test
	@DisplayName("searchMovies: delegates to repository and maps list")
	void searchMovies_returnsList() {
		String query = "in";
		Movie m1 = movie(1L, "Inception", "Sci-Fi", "English", 148, "desc1", 8.8, "p1");
		Movie m2 = movie(2L, "Inside Out", "Animation", "English", 95, "desc2", 8.1, "p2");
		when(movieRepository.findByMovieNameContainingIgnoreCase(query)).thenReturn(Arrays.asList(m1, m2));

		List<MovieDto> list = movieService.searchMovies(query);

		assertEquals(2, list.size());
		assertEquals("Inception", list.get(0).getMovieName());
		assertEquals("Inside Out", list.get(1).getMovieName());
		verify(movieRepository).findByMovieNameContainingIgnoreCase(query);
	}

	@Test
	@DisplayName("saveOrUpdateMovie: create (movieId null) -> saves new entity and returns DTO with generated id")
	void saveOrUpdateMovie_create() {
		MovieDto input = dto(null, "Dune", "Sci-Fi", "English", 155, "Arrakis saga", 8.2, "poster-url");

		when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> {
			Movie toSave = inv.getArgument(0);
			toSave.setMovieId(123L); // simulate generated ID
			return toSave;
		});

		MovieDto saved = movieService.saveOrUpdateMovie(input);

		assertNotNull(saved.getMovieId());
		assertEquals(123L, saved.getMovieId());
		assertEquals("Dune", saved.getMovieName());

		ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);
		verify(movieRepository).save(captor.capture());
		Movie persisted = captor.getValue();
		assertEquals("Dune", persisted.getMovieName());
		assertEquals("Sci-Fi", persisted.getGenre());
		assertEquals(155, persisted.getDuration());
		assertEquals("Arrakis saga", persisted.getDescription());
		assertEquals(8.2, persisted.getRating());
		assertEquals("poster-url", persisted.getPosterUrl());
	}

	@Test
	@DisplayName("saveOrUpdateMovie: update (movieId present & found) -> merges onto existing and saves")
	void saveOrUpdateMovie_update_found() {
		Movie existing = movie(200L, "Old Name", "Old Genre", "Old Lang", 100, "Old Desc", 6.0, "old-url");
		when(movieRepository.findById(200L)).thenReturn(Optional.of(existing));
		when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

		MovieDto changes = dto(200L, "New Name", "New Genre", "New Lang", 120, "New Desc", 7.5, "new-url");

		MovieDto updated = movieService.saveOrUpdateMovie(changes);

		assertEquals(200L, updated.getMovieId());
		assertEquals("New Name", updated.getMovieName());
		assertEquals("New Genre", updated.getGenre());
		assertEquals("New Lang", updated.getLanguage());
		assertEquals(120, updated.getDuration());
		assertEquals("New Desc", updated.getDescription());
		assertEquals(7.5, updated.getRating());
		assertEquals("new-url", updated.getPosterUrl());

		verify(movieRepository).findById(200L);
		verify(movieRepository).save(existing); // same instance updated and saved
	}

	@Test
	@DisplayName("saveOrUpdateMovie: update (movieId present but not found) -> creates new entity")
	void saveOrUpdateMovie_update_idNotFound_createsNew() {
		when(movieRepository.findById(777L)).thenReturn(Optional.empty());
		when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> {
			Movie m = inv.getArgument(0);
			m.setMovieId(888L);
			return m;
		});

		MovieDto input = dto(777L, "Brand New", "Genre", "Lang", 111, "Desc", 9.0, "poster");

		MovieDto out = movieService.saveOrUpdateMovie(input);

		assertEquals(888L, out.getMovieId());
		assertEquals("Brand New", out.getMovieName());

		verify(movieRepository).findById(777L);
		verify(movieRepository).save(any(Movie.class));
	}

	@Test
	@DisplayName("deleteMovie: calls repository.deleteById")
	void deleteMovie_delegates() {
		doNothing().when(movieRepository).deleteById(9L);

		movieService.deleteMovie(9L);

		verify(movieRepository).deleteById(9L);
	}

// Negative Test case.    

	@Test
	@DisplayName("getMovieById: null id -> throws NullPointerException (due to .map on null Optional)")
	void getMovieById_nullId_throwsNPE() {
		// Explicitly ensure mock returns null for clarity (optional but stable)
		doReturn(null).when(movieRepository).findById(null);

		assertThrows(NullPointerException.class, () -> movieService.getMovieById(null));

		// Use a typed matcher for robustness
		verify(movieRepository, times(1)).findById(isNull());
	}

	@Test
	@DisplayName("getMovieById: repository throws -> service propagates")
	void getMovieById_repoThrows_propagates() {
		// Arrange
		when(movieRepository.findById(1L)).thenThrow(new RuntimeException("DB down"));

		// Act + Assert
		RuntimeException ex = assertThrows(RuntimeException.class, () -> movieService.getMovieById(1L));
		assertEquals("DB down", ex.getMessage());

		// Verify
		verify(movieRepository, times(1)).findById(1L);
		verifyNoMoreInteractions(movieRepository);
	}

}