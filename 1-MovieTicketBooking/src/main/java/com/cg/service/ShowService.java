package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.ShowDto;
import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.entity.Theatre;
import com.cg.repository.MovieRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.TheatreRepository;

@Service
public class ShowService {

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TheatreRepository theatreRepository;

	// ================= CREATE =================
	public ShowDto addShow(ShowDto showDto) {
		// Resolve associations from IDs in DTO
		Movie movie = movieRepository.findById(showDto.getMovieId())
				.orElseThrow(() -> new RuntimeException("Movie not found"));
		Theatre theatre = theatreRepository.findById(showDto.getTheatreId())
				.orElseThrow(() -> new RuntimeException("Theatre not found"));

		// Manual DTO -> Entity
		Show show = new Show();
		show.setShowDate(showDto.getShowDate());
		show.setShowTime(showDto.getShowTime());
		show.setPrice(showDto.getPrice());
		show.setMovie(movie);
		show.setTheatre(theatre);

		Show saved = showRepository.save(show);

		// Manual Entity -> DTO
		return toDto(saved);
	}

	// ================= UPDATE =================
	public ShowDto updateShow(Long id, ShowDto updated) {

		Show show = showRepository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));

		show.setShowDate(updated.getShowDate());
		show.setShowTime(updated.getShowTime());
		show.setPrice(updated.getPrice());

		// Update associations if IDs are provided
		if (updated.getMovieId() != null) {
			Movie movie = movieRepository.findById(updated.getMovieId())
					.orElseThrow(() -> new RuntimeException("Movie not found"));
			show.setMovie(movie);
		}
		if (updated.getTheatreId() != null) {
			Theatre theatre = theatreRepository.findById(updated.getTheatreId())
					.orElseThrow(() -> new RuntimeException("Theatre not found"));
			show.setTheatre(theatre);
		}

		Show saved = showRepository.save(show);
		return toDto(saved);
	}

	// ================= DELETE =================
	public void deleteShow(Long id) {
		showRepository.deleteById(id);
	}

	// ================= GET BY ID =================
	public ShowDto getShowById(Long id) {
		Show show = showRepository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));
		return toDto(show);
	}

	// ================= USER SIDE (IMPORTANT) =================
	// Fetch shows ONLY for selected movie
	public List<ShowDto> getShowsByMovie(Long movieId) {
		return showRepository.findByMovieMovieId(movieId).stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<ShowDto> getAllShows() {
		return showRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	// ===== Manual mapping methods (no mapper class) =====
	private ShowDto toDto(Show show) {
		ShowDto dto = new ShowDto();
		dto.setShowId(show.getShowId());
		dto.setShowDate(show.getShowDate());
		dto.setShowTime(show.getShowTime());
		dto.setPrice(show.getPrice());
		dto.setMovieId(show.getMovie() != null ? show.getMovie().getMovieId() : null);
		dto.setTheatreId(show.getTheatre() != null ? show.getTheatre().getTheatreId() : null);
		dto.setMovieName(show.getMovie().getMovieName());
		dto.setTheatreName(show.getTheatre().getTheatreName());

		return dto;
	}
}
