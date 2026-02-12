package com.cg.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.MovieDto;
import com.cg.dto.ShowDto;
import com.cg.dto.TheatreDto;
import com.cg.entity.Show;
import com.cg.service.MovieService;
import com.cg.service.ShowService;
import com.cg.service.TheatreService;

import jakarta.validation.Valid;

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
    public String showTimings(
            @PathVariable Long movieId,
            @RequestParam(required = false) LocalDate date,
            Model model) {
 
        // Get movie (DTO or entity depending on your service)
        MovieDto movie = movieService.getMovieById(movieId);
 
        // All shows for the movie (DTOs)
        List<ShowDto> allShows = showService.getShowsByMovie(movieId);
 
        // Available dates (distinct, sorted)
        List<LocalDate> dates = allShows.stream()
                .map(ShowDto::getShowDate)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
 
        // selectedDate defaults to first available date if not provided
        LocalDate selectedDate = (date == null && !dates.isEmpty()) ? dates.get(0) : date;
 
        // Filter shows for the selected date
        List<ShowDto> showsForDate = (selectedDate == null) ? List.of()
                : allShows.stream()
                        .filter(s -> selectedDate.equals(s.getShowDate()))
                        .toList();
 
        // Group by theatreId
        Map<Long, List<ShowDto>> groupedShows = showsForDate.stream()
                .collect(Collectors.groupingBy(ShowDto::getTheatreId));
 
        model.addAttribute("movie", movie);
        model.addAttribute("dates", dates);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("groupedShows", groupedShows);
 
        return "show-timings";
    }

    // API-style endpoint to create a Show and auto-generate seats
    @PostMapping("/shows")
    @ResponseBody
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

    // CREATE (POST)
    @PostMapping("/admin/shows")
    public String addShow(@Valid @ModelAttribute("show") ShowDto showDto,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("theatres", theatreService.getAllTheatres());
            return "admin/admin-show-form";
        }
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

    // UPDATE (PUT)
    @PutMapping("/admin/shows/{id}")
    public String updateShow(@PathVariable Long id,
                             @Valid @ModelAttribute("show") ShowDto showDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("theatres", theatreService.getAllTheatres());
            return "admin/admin-show-form";
        }
        showService.updateShow(id, showDto);
        return "redirect:/admin/shows";
    }

    // DELETE (DELETE)
    @DeleteMapping("/admin/shows/{id}")
    public String deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return "redirect:/admin/shows";
    }
}