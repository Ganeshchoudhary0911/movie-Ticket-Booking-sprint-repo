package com.cg.service;

import com.cg.dto.ShowDto;
import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.entity.Theatre;
import com.cg.repository.MovieRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.TheatreRepository;
import com.cg.service.ShowService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

	@Mock
	private ShowRepository showRepository;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private TheatreRepository theatreRepository;

	@InjectMocks
	private ShowService showService;

	private Movie movie;
	private Theatre theatre;
	private Show show;

	// ============ SETUP ============
	@BeforeEach
	void setup() {

		movie = new Movie();
		movie.setMovieId(1L);
		movie.setMovieName("KGF");

		theatre = new Theatre();
		theatre.setTheatreId(1L);
		theatre.setTheatreName("PVR");

		show = new Show();
		show.setShowId(1L);
		show.setShowDate(LocalDate.now());
		show.setShowTime(LocalTime.NOON);
		show.setPrice(200);
		show.setMovie(movie);
		show.setTheatre(theatre);
	}

	// ============ ADD ============
	public ShowDto addShow(ShowDto dto) {

	    Movie movie = movieRepository.findById(dto.getMovieId())
	            .orElseThrow(() -> new RuntimeException("Movie not found"));

	    Theatre theatre = theatreRepository.findById(dto.getTheatreId())
	            .orElseThrow(() -> new RuntimeException("Theatre not found"));

	    Show show = new Show();
	    show.setShowDate(dto.getShowDate());
	    show.setShowTime(dto.getShowTime());
	    show.setPrice(dto.getPrice());
	    show.setMovie(movie);
	    show.setTheatre(theatre);

	    Show saved = showRepository.save(show);

	    ShowDto result = new ShowDto();
	    result.setShowId(saved.getShowId());
	    result.setShowDate(saved.getShowDate());
	    result.setShowTime(saved.getShowTime());
	    result.setPrice(saved.getPrice());

	    result.setMovieId(movie.getMovieId());
	    result.setMovieName(movie.getMovieName());

	    result.setTheatreId(theatre.getTheatreId());
	    result.setTheatreName(theatre.getTheatreName());

	    return result;
	}
	// ============ GET ALL ============
	@Test
	void getAllShows_shouldReturnList() {
		when(showRepository.findAll()).thenReturn(List.of(show));
		List<ShowDto> list = showService.getAllShows();
		assertEquals(1, list.size());
		assertEquals("KGF", list.get(0).getMovieName());
	}

	// ============ GET BY ID ============
	@Test
	void getShowById_shouldReturnDto() {
		when(showRepository.findById(1L)).thenReturn(Optional.of(show));
		ShowDto dto = showService.getShowById(1L);
		assertEquals(1L, dto.getShowId());
	}
}
