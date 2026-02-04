package com.cg.controller;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.service.MovieService;
import com.cg.service.ShowService;
 
@Controller
public class ShowController {
 
    @Autowired
    private ShowService showService;
 
    @Autowired
    private MovieService movieService;
 
    // ================= USER =================
    // Show timings page
    @GetMapping("/shows/{movieId}")
    public String showTimings(@PathVariable Long movieId, Model model) {
 
        // Get movie
        Movie movie = movieService.getMovieById(movieId);
 
        // IMPORTANT: Get shows only for this movie
        List<Show> shows = showService.getShowsByMovie(movieId);
 
        // Group by theatre ID (NOT theatre object)
        Map<Long, List<Show>> groupedShows =
                shows.stream().collect(Collectors.groupingBy(
                        show -> show.getTheatre().getTheatreId()
                ));
 
        model.addAttribute("movie", movie);
        model.addAttribute("groupedShows", groupedShows);
 
        return "show-timings";
    }
}
 




//package com.cg.controller;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.cg.entity.Movie;
//import com.cg.entity.Show;
//import com.cg.service.MovieService;
//import com.cg.service.ShowService;
//import com.cg.service.TheatreService;
//
//@Controller
//public class ShowController {
//
//	@Autowired
//	private ShowService showService;
//
//	@Autowired
//	private MovieService movieService;
//
//	@Autowired
//	private TheatreService theatreService;
//
//	// ================= USER =================
//
//	@GetMapping("/shows/{movieId}")
//	public String showTimings(@PathVariable Long movieId, Model model) {
//	 
//	    Movie movie = movieService.getMovieById(movieId);
//	 
//	    // IMPORTANT: must return shows ONLY for this movie
//	    List<Show> shows = showService.getShowById
//	 
//	    // Group by theatre ID (NOT theatre object)
//	    Map<Long, List<Show>> groupedShows =
//	            shows.stream().collect(Collectors.groupingBy(
//	                    show -> show.getTheatre().getTheatreId()
//	            ));
//	 
//	    model.addAttribute("movie", movie);
//	    model.addAttribute("groupedShows", groupedShows);
//	 
//	    return "show-timings";
//	}
//	 
//
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
