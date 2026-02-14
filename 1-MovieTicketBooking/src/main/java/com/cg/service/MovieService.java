package com.cg.service;

import com.cg.dto.MovieDto;
import com.cg.entity.Movie;
import com.cg.exception.MovieNotFoundException;
import com.cg.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    // ===== READ ALL =====
    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===== READ BY ID =====
    @Override
    public MovieDto getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    // ===== SEARCH =====
    @Override
    public List<MovieDto> searchMovies(String name) {
        List<MovieDto> movies = movieRepository.findByMovieNameContainingIgnoreCase(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        if (movies.isEmpty()) {
            throw new MovieNotFoundException("No movies found matching the name: " + name);
        }
        
        return movies;
    }

    // ===== CREATE / UPDATE =====
    @Override
    public MovieDto saveOrUpdateMovie(MovieDto dto) {
        // For update, load existing; for create, instantiate new
        Movie entity = (dto.getMovieId() != null)
                ? movieRepository.findById(dto.getMovieId()).orElse(new Movie())
                : new Movie();

        // Apply fields from DTO -> Entity
        entity.setMovieName(dto.getMovieName());
        entity.setGenre(dto.getGenre());
        entity.setLanguage(dto.getLanguage());
        entity.setDuration(dto.getDuration());
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setPosterUrl(dto.getPosterUrl());

        Movie saved = movieRepository.save(entity);
        return toDto(saved);
    }

    // ===== DELETE =====
    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // (Optional helper if you still need Optional<MovieDto> in some places)
    public Optional<MovieDto> findById(Long id) {
        return movieRepository.findById(id).map(this::toDto);
    }

    // ===== Inline mapping =====
    private MovieDto toDto(Movie m) {
        if (m == null) return null;
        MovieDto dto = new MovieDto();
        dto.setMovieId(m.getMovieId());
        dto.setMovieName(m.getMovieName());
        dto.setGenre(m.getGenre());
        dto.setLanguage(m.getLanguage());
        dto.setDuration(m.getDuration());
        dto.setDescription(m.getDescription());
        dto.setRating(m.getRating());
        dto.setPosterUrl(m.getPosterUrl());
        return dto;
    }

    @SuppressWarnings("unused")
    private Movie toEntity(MovieDto dto) {
        if (dto == null) return null;
        Movie m = new Movie();
        m.setMovieId(dto.getMovieId());
        m.setMovieName(dto.getMovieName());
        m.setGenre(dto.getGenre());
        m.setLanguage(dto.getLanguage());
        m.setDuration(dto.getDuration());
        m.setDescription(dto.getDescription());
        m.setRating(dto.getRating());
        m.setPosterUrl(dto.getPosterUrl());
        return m;
    }
}