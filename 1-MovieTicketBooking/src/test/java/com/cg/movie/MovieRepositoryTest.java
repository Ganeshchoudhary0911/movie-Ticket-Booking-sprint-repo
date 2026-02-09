package com.cg.movie;

import com.cg.entity.Movie;
import com.cg.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;
        @Test
        void testSave() {

            Movie movie = new Movie();
            movie.setMovieName("Test");
            movie.setRating(8.5);

            Movie saved = movieRepository.save(movie);

            assertNotNull(saved.getMovieId());
        }
    }

    // =========================
    // SAVE TEST
    // =========================
//    @Test
//    void saveMovie_shouldSaveSuccessfully() {
//
//        Movie movie = new Movie();
//        movie.setMovieName("Inception");
//        movie.setGenre("Sci-Fi");
//        movie.setLanguage("English");
//        movie.setDuration(150);
//        movie.setDescription("Mind bending movie");
//        movie.setRating(8.8);   // ✅ double
//
//        Movie saved = movieRepository.save(movie);
//
//        assertThat(saved.getMovieId()).isNotNull();
//        assertThat(saved.getMovieName()).isEqualTo("Inception");
//        assertThat(saved.getRating()).isEqualTo(8.8);
//    }
//
//    // =========================
//    // FIND BY ID
//    // =========================
//    @Test
//    void findById_shouldReturnMovie() {
//
//        Movie movie = new Movie();
//        movie.setMovieName("Avatar");
//        movie.setRating(7.5);
//
//        Movie saved = movieRepository.save(movie);
//
//        Optional<Movie> found = movieRepository.findById(saved.getMovieId());
//
//        assertThat(found).isPresent();
//        assertThat(found.get().getRating()).isEqualTo(7.5);
//    }
//
//    // =========================
//    // FIND ALL
//    // =========================
//    @Test
//    void findAll_shouldReturnAllMovies() {
//
//        Movie m1 = new Movie();
//        m1.setMovieName("A");
//        m1.setRating(6.5);
//
//        Movie m2 = new Movie();
//        m2.setMovieName("B");
//        m2.setRating(9.0);
//
//        movieRepository.save(m1);
//        movieRepository.save(m2);
//
//        List<Movie> movies = movieRepository.findAll();
//
//        assertThat(movies.size()).isEqualTo(2);
//    }
//
//    // =========================
//    // DELETE
//    // =========================
//    @Test
//    void delete_shouldRemoveMovie() {
//
//        Movie movie = new Movie();
//        movie.setMovieName("DeleteMe");
//        movie.setRating(5.0);
//
//        Movie saved = movieRepository.save(movie);
//
//        movieRepository.deleteById(saved.getMovieId());
//
//        Optional<Movie> deleted = movieRepository.findById(saved.getMovieId());
//
//        assertThat(deleted).isEmpty();
//    }
//}
