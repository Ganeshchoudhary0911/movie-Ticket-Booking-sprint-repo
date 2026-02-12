package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.MovieDto;
import com.cg.service.MovieService;

import jakarta.validation.Valid;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "search", required = false) String search) {
        List<MovieDto> movies = (search != null && !search.isBlank())
                ? movieService.searchMovies(search)
                : movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "home";
    }

    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        if (movie == null) return "redirect:/?error=movie-not-found";
        model.addAttribute("movie", movie);
        return "movie-details";
    }

    /* ------------------ ADMIN ------------------ */

    @GetMapping("/admin/movies")
    public String adminMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("movie", new MovieDto());
        return "admin/admin-movie";
    }

    @PostMapping("/admin/movies")
    public String createMovie(
            @Valid @ModelAttribute("movie") MovieDto movie,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "admin/admin-movie-form";  // return the SAME form
        }

        movieService.saveOrUpdateMovie(movie);
        return "redirect:/admin/movies";
    }

    @PutMapping("/admin/movies/{id}")
    public String updateMovie(
            @PathVariable Long id,
            @ModelAttribute("movie") MovieDto movie,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "admin/admin-movie-form";
        }

        movie.setMovieId(id);
        movieService.saveOrUpdateMovie(movie);
        return "redirect:/admin/movies";
    }

    @DeleteMapping("/admin/movies/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }

    @GetMapping("/admin/movies/new")
    public String movieCreateForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        model.addAttribute("ratings", List.of("U", "UA", "A"));
        return "admin/admin-movie-form";
    }

    @GetMapping("/admin/movies/{id}/edit")
    public String movieEditForm(@PathVariable Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        if (movie == null) return "redirect:/admin/movies?error=movie-not-found";

        model.addAttribute("movie", movie);
        model.addAttribute("ratings", List.of("U", "UA", "A"));
        return "admin/admin-movie-form";
    }
}