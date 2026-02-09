package com.cg.show;

import com.cg.controller.ShowController;
import com.cg.dto.MovieDto;
import com.cg.repository.UserRepository;
import com.cg.service.MovieService;
import com.cg.service.ShowService;
import com.cg.service.TheatreService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShowController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class
})
class ShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ShowService showService;
    @MockBean private MovieService movieService;
    @MockBean private TheatreService theatreService;
    @MockBean private UserRepository userRepository;

    @Test
    void testAdminShowsPage() throws Exception {

        when(showService.getAllShows()).thenReturn(List.of());
        when(movieService.getAllMovies()).thenReturn(List.of());
        when(theatreService.getAllTheatres()).thenReturn(List.of());

        mockMvc.perform(get("/admin/shows"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-show"));
    }

    @Test
    void testShowTimingsPage() throws Exception {

        MovieDto movie = new MovieDto();
        movie.setMovieId(1L);

        when(movieService.getMovieById(1L)).thenReturn(movie);
        when(showService.getShowsByMovie(1L)).thenReturn(List.of());

        mockMvc.perform(get("/shows/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("show-timings"));
    }
}
