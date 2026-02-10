package com.cg.movie;

import com.cg.entity.Movie;
import com.cg.repository.MovieRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach; // Har test se pehle clean up ke liye
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll(); // Safety ke liye har test se pehle database saaf
    }

    @Test
    void saveMovie_shouldSaveSuccessfully() {
        Movie movie = new Movie();
        movie.setMovieName("Inception");
        movie.setRating(8.8);

        Movie saved = movieRepository.save(movie);

        assertThat(saved.getMovieId()).isNotNull();
        assertThat(saved.getMovieName()).isEqualTo("Inception");
    }

    @Test
    void findById_shouldReturnMovie() {
        Movie movie = new Movie();
        movie.setMovieName("Avatar");
        movie.setRating(7.5);
        Movie saved = movieRepository.save(movie);

        Optional<Movie> found = movieRepository.findById(saved.getMovieId());

        assertThat(found).isPresent();
        assertThat(found.get().getRating()).isEqualTo(7.5);
    }

//    @Test
//    void findAll_shouldReturnAllMovies() {
//        Movie m1 = new Movie();
//        m1.setMovieName("A"); m1.setRating(6.5);
//        Movie m2 = new Movie();
//        m2.setMovieName("B"); m2.setRating(9.0);
//
//        movieRepository.save(m1);
//        movieRepository.save(m2);
//
//        List<Movie> movies = movieRepository.findAll();
//
//        // size 2 hi hona chahiye agar database empty tha starting mein
//        assertThat(movies).hasSize(2);
//        assertThat(movies).extracting(Movie::getMovieName).containsExactlyInAnyOrder("A", "B");
//    }

    @Test
    void delete_shouldRemoveMovie() {
        Movie movie = new Movie();
        movie.setMovieName("DeleteMe");
        movie.setRating(5.0);
        Movie saved = movieRepository.save(movie);

        movieRepository.deleteById(saved.getMovieId());
        Optional<Movie> deleted = movieRepository.findById(saved.getMovieId());

        assertThat(deleted).isEmpty();
    }

//    @Test
//    void findByMovieNameContainingIgnoreCase_shouldReturnMatches() {
//        Movie m1 = new Movie(); m1.setMovieName("Inception");
//        Movie m2 = new Movie(); m2.setMovieName("Inside Out");
//        Movie m3 = new Movie(); m3.setMovieName("Avatar");
//
//        movieRepository.save(m1);
//        movieRepository.save(m2);
//        movieRepository.save(m3);
//
//        List<Movie> result = movieRepository.findByMovieNameContainingIgnoreCase("In");
//
//        assertThat(result).hasSize(2);
//        assertThat(result).extracting(Movie::getMovieName)
//                .containsExactlyInAnyOrder("Inception", "Inside Out");
//    }
}
