package com.cg.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.service.MovieService;
import com.cg.service.ShowService;

@Controller
public class ShowController {
	@Autowired
	private MovieService movieService;
	@Autowired
	private ShowService showService;

	@GetMapping("/shows/{movieId}")
	public String showTimings(
	        @PathVariable Long movieId,
	        @RequestParam(required = false) LocalDate date,
	        Model model) {
	 
	    Movie movie = movieService.getMovieById(movieId);
	 
	    // 1️⃣ Get all shows for movie
	    List<Show> allShows = showService.getShowsByMovie(movieId);
	 
	    // 2️⃣ Extract available dates from DB
	    List<LocalDate> dates = allShows.stream()
	            .map(Show::getShowDate)
	            .distinct()
	            .sorted()
	            .toList();
	 
	    // 3️⃣ Decide selected date
	    LocalDate selectedDate;
	    if (date == null && !dates.isEmpty()) {
	        selectedDate = dates.get(0);
	    } else {
	        selectedDate = date;
	    }
	 
	    // 4️⃣ Filter shows by selected date
	    List<Show> showsForDate = allShows.stream()
	            .filter(s -> s.getShowDate().equals(selectedDate))
	            .toList();
	 
	    // 5️⃣ Group shows by theatre
	    Map<Long, List<Show>> groupedShows =
	            showsForDate.stream()
	                    .collect(Collectors.groupingBy(
	                            s -> s.getTheatre().getTheatreId()
	                    ));
	 
	    model.addAttribute("movie", movie);
	    model.addAttribute("dates", dates);
	    model.addAttribute("selectedDate", selectedDate);
	    model.addAttribute("groupedShows", groupedShows);
	 
	    return "show-timings";
	}
}






//	// ================= ADMIN =================

//

//	@GetMapping("/admin/shows")

//	public String adminShowList(Model model) {

//		model.addAttribute("shows", showService.getAllShows());

//		return "admin/admin-show";

//	}

//

//	@GetMapping("/admin/shows/add")

//	public String addShowForm(Model model) {

//		model.addAttribute("show", new Show());

//		model.addAttribute("movies", movieService.getAllMovies());

//		model.addAttribute("theatres", theatreService.getAllTheatres());

//		return "admin/admin-show-form";

//	}

//

//	@PostMapping("/admin/shows/add")

//	public String addShow(@ModelAttribute Show show) {

//		showService.addShow(show);

//		return "redirect:/admin/shows";

//	}

//

//	@GetMapping("/admin/shows/edit/{id}")

//	public String editShow(@PathVariable Long id, Model model) {

//		model.addAttribute("show", showService.getShowById(id));

//		model.addAttribute("movies", movieService.getAllMovies());

//		model.addAttribute("theatres", theatreService.getAllTheatres());

//		return "admin/admin-show-form";

//	}

//

//	@PostMapping("/admin/shows/update/{id}")

//	public String updateShow(@PathVariable Long id, @ModelAttribute Show show) {

//		showService.updateShow(id, show);

//		return "redirect:/admin/shows";

//	}

//

//	@GetMapping("/admin/shows/delete/{id}")

//	public String deleteShow(@PathVariable Long id) {

//		showService.deleteShow(id);

//		return "redirect:/admin/shows";

//	}

//}

 