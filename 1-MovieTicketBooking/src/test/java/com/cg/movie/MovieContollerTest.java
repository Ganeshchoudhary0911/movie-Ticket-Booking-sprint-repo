
package com.cg.movie;
 
import com.cg.controller.MovieController;
import com.cg.dto.MovieDto;
import com.cg.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
 
@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = true) // ← ensure security/CSRF filters run
@WithMockUser(roles = "ADMIN")
class MovieControllerTest {
 
	@Autowired
	private MockMvc mockMvc;
 
	@MockBean
	private MovieService movieService;
 
	// -------------------------
	// HOME PAGE
	// -------------------------
 
	@Test
	void home_withoutSearch_returnsHomeView() throws Exception {
 
		when(movieService.getAllMovies()).thenReturn(List.of(new MovieDto()));
 
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"))
				.andExpect(model().attributeExists("movies"));
 
		verify(movieService).getAllMovies();
	}
 
	@Test
	void home_withSearch_callsSearchMovies() throws Exception {
 
		when(movieService.searchMovies("abc")).thenReturn(List.of());
 
		mockMvc.perform(get("/").param("search", "abc")).andExpect(status().isOk()).andExpect(view().name("home"));
 
		verify(movieService).searchMovies("abc");
	}
 
	// -------------------------
	// DETAILS
	// -------------------------
 
//	@Test
//	void movieDetails_found_returnsView() throws Exception {
// 
//		MovieDto dto = new MovieDto();
//		dto.setMovieId(1L);
// 
//		when(movieService.getMovieById(1L)).thenReturn(dto);
// 
//		mockMvc.perform(get("/movie/1")).andExpect(status().isOk()).andExpect(view().name("movie-details"))
//				.andExpect(model().attributeExists("movie"));
//	}

 
	@Test
	void movieDetails_notFound_redirects() throws Exception {
 
		when(movieService.getMovieById(99L)).thenReturn(null);
 
		mockMvc.perform(get("/movie/99")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/?error=movie-not-found"));
	}
 
	// -------------------------
	// ADMIN LIST
	// -------------------------
 
	@Test
	void adminMovies_returnsView() throws Exception {
 
		when(movieService.getAllMovies()).thenReturn(List.of());
 
		mockMvc.perform(get("/admin/movies")).andExpect(status().isOk()).andExpect(view().name("admin/admin-movie"))
				.andExpect(model().attributeExists("movies")).andExpect(model().attributeExists("movie"));
	}
 
	// -------------------------
	// SAVE
	// -------------------------
 
	@Test
	void saveMovie_redirects() throws Exception {
 
		mockMvc.perform(post("/admin/movies/save").with(csrf()) // VERY IMPORTANT
				.param("movieName", "TestMovie")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/movies"));
 
		verify(movieService).saveOrUpdateMovie(any());
	}
 
	// -------------------------
	// DELETE
	// -------------------------
 
	@Test
	void deleteMovie_redirects() throws Exception {
 
		mockMvc.perform(get("/admin/movies/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/movies"));
 
		verify(movieService).deleteMovie(1L);
	}
 
	// -------------------------
	// NEW FORM
	// -------------------------
 
	@Test
	void newForm_returnsView() throws Exception {
 
		mockMvc.perform(get("/admin/movies/new")).andExpect(status().isOk())
				.andExpect(view().name("admin/admin-movie-form")).andExpect(model().attributeExists("movie"))
				.andExpect(model().attributeExists("ratings"));
	}
 
	// -------------------------
	// EDIT
	// -------------------------
 
	@Test
	void edit_found_returnsView() throws Exception {
 
		when(movieService.getMovieById(1L)).thenReturn(new MovieDto());
 
		mockMvc.perform(get("/admin/movies/1/edit")).andExpect(status().isOk())
				.andExpect(view().name("admin/admin-movie-form"));
	}
 
	@Test
	void edit_notFound_redirects() throws Exception {
 
		when(movieService.getMovieById(1L)).thenReturn(null);
 
		mockMvc.perform(get("/admin/movies/1/edit")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/movies?error=movie-not-found"));
	}
 
// Negative Test Case.
	@WithMockUser(roles = "USER")
	@Test
	void saveMovie_withoutAdminRole_forbidden() throws Exception {
		mockMvc.perform(post("/admin/movies/save").param("movieName", "Test")).andExpect(status().isForbidden());
	}
 
}