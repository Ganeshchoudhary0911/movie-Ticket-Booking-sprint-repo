package com.cg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cg.entity.Movie;
import com.cg.service.MovieService;

@Controller
public class MovieController {
    @Autowired
    MovieService movieService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "search", required = false) String search) {
        model.addAttribute("movies", (search != null) ? movieService.searchMovies(search) : movieService.getAllMovies());
        return "home";
    }

    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        return "movie-details";
    }

    @GetMapping("/admin/movies")
    public String adminMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("movie", new Movie());
        return "admin/admin-movie";
    }

    @PostMapping("/admin/movies/save")
    public String saveMovie(@ModelAttribute Movie movie) {
        movieService.saveOrUpdateMovie(movie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }
}