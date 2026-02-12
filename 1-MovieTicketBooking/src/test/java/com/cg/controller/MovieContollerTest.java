package com.cg.controller;

import com.cg.controller.MovieController;
import com.cg.dto.MovieDto;
import com.cg.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MovieController Tests (6 test methods)
 */
@WebMvcTest(MovieController.class)
@WithMockUser(roles = "ADMIN")
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    private MovieDto validMovie;

    @BeforeEach
    void setUp() {
        validMovie = new MovieDto();
        validMovie.setMovieName("Avatar");
        validMovie.setGenre("Science Fiction");
        validMovie.setLanguage("English");
        validMovie.setDuration(162);
        validMovie.setRating(8.5);
        validMovie.setPosterUrl("https://example.com/avatar.jpg");
    }

    @Test
    @DisplayName("POST /admin/movies - Valid movie redirects to admin list")
    void shouldCreateMovie_withValidData_redirect() throws Exception {
        mockMvc.perform(post("/admin/movies")
                .with(csrf())
                .param("movieName", validMovie.getMovieName())
                .param("genre", validMovie.getGenre())
                .param("language", validMovie.getLanguage())
                .param("duration", String.valueOf(validMovie.getDuration()))
                .param("rating", String.valueOf(validMovie.getRating()))
                .param("posterUrl", validMovie.getPosterUrl()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).saveOrUpdateMovie(any(MovieDto.class));
    }

    @Test
    @DisplayName("POST /admin/movies - Invalid movie returns form with errors")
    void shouldCreateMovie_withInvalidData_returnForm() throws Exception {
        mockMvc.perform(post("/admin/movies")
                .with(csrf())
                .param("movieName", "")
                .param("genre", "Action")
                .param("duration", "120")
                .param("rating", "8.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-movie-form"));

        verify(movieService, never()).saveOrUpdateMovie(any());
    }

    @Test
    @DisplayName("GET /admin/movies/new - Returns form with empty movie")
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/movies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-movie-form"))
                .andExpect(model().attributeExists("movie", "ratings"));
    }

    @Test
    @DisplayName("GET /admin/movies/{id}/edit - Found movie returns edit form")
    void shouldShowEditForm_whenMovieFound() throws Exception {
        MovieDto existingMovie = new MovieDto();
        existingMovie.setMovieId(1L);
        existingMovie.setMovieName("Existing Movie");

        when(movieService.getMovieById(1L)).thenReturn(existingMovie);

        mockMvc.perform(get("/admin/movies/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-movie-form"))
                .andExpect(model().attributeExists("movie", "ratings"));

        verify(movieService).getMovieById(1L);
    }

    @Test
    @DisplayName("GET /admin/movies/{id}/edit - Not found movie redirects")
    void shouldRedirect_whenMovieNotFound() throws Exception {
        when(movieService.getMovieById(99L)).thenReturn(null);

        mockMvc.perform(get("/admin/movies/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies?error=movie-not-found"));
    }

    @Test
    @DisplayName("GET / - Home page returns all movies")
    void shouldShowHomePage_withAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(validMovie));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("movies"));

        verify(movieService).getAllMovies();
    }
}