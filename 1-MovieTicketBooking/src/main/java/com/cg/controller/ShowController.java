package com.cg.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.MovieDto;
import com.cg.dto.ShowDto;
import com.cg.dto.TheatreDto;
import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.service.MovieService;
import com.cg.service.ShowService;
import com.cg.service.TheatreService;

@Controller
public class ShowController {

    @Autowired
    private ShowService showService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheatreService theatreService;


    // ========== USER ==========
    @GetMapping("/shows/{movieId}")
    public String showTimings(@PathVariable Long movieId, Model model) {

        MovieDto movie = movieService.getMovieById(movieId);

        // DTOs instead of entities
        List<ShowDto> shows = showService.getShowsByMovie(movieId);

        // Group by theatreId directly from DTO
        Map<Long, List<ShowDto>> groupedShows =
                shows.stream().collect(Collectors.groupingBy(ShowDto::getTheatreId));

        model.addAttribute("movie", movie);
        model.addAttribute("groupedShows", groupedShows);

        return "show-timings";
    }
    
    @PostMapping("/shows")
    public Show createShow(@RequestBody Show show) {
        return showService.createShowWithSeats(show);
    }


    // ========== ADMIN ==========
    @GetMapping("/admin/shows")
    public String adminShowList(Model model) {
        var shows = showService.getAllShows();
        var movies = movieService.getAllMovies();
        var theatres = theatreService.getAllTheatres();

        Map<Long, String> movieNames = movies.stream()
            .collect(Collectors.toMap(MovieDto::getMovieId, MovieDto::getMovieName));

        Map<Long, String> theatreNames = theatres.stream()
            .collect(Collectors.toMap(TheatreDto::getTheatreId, TheatreDto::getTheatreName));

        model.addAttribute("shows", shows);
        model.addAttribute("movieNames", movieNames);
        model.addAttribute("theatreNames", theatreNames);
        return "admin/admin-show";
    }

    @GetMapping("/admin/shows/add")
    public String addShowForm(Model model) {
        model.addAttribute("show", new ShowDto()); // bind to DTO
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/admin-show-form";
    }

    @PostMapping("/admin/shows/add")
    public String addShow(@ModelAttribute("show") ShowDto showDto) {
        showService.addShow(showDto);
        return "redirect:/admin/shows";
    }

    @GetMapping("/admin/shows/edit/{id}")
    public String editShow(@PathVariable Long id, Model model) {
        model.addAttribute("show", showService.getShowById(id)); // ShowDto
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/admin-show-form";
    }

    @PostMapping("/admin/shows/update/{id}")
    public String updateShow(@PathVariable Long id, @ModelAttribute("show") ShowDto showDto) {
        showService.updateShow(id, showDto);
        return "redirect:/admin/shows";
    }

    @GetMapping("/admin/shows/delete/{id}")
    public String deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return "redirect:/admin/shows";
    }
}