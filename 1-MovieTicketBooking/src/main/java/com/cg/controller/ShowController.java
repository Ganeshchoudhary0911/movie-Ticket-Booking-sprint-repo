package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cg.entity.*;
import com.cg.entity.Theatre;
import com.cg.service.*;

@Controller
public class ShowController {

	@Autowired
	private ShowService showService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private TheatreService theatreService;

	// ================= USER =================

	@GetMapping("/shows/{theatreId}")
	public String viewShowTimings(@PathVariable Long theatreId, Model model) {

		Theatre theatre = theatreService.getTheatreById(theatreId);

		model.addAttribute("theatre", theatre);
		model.addAttribute("shows", showService.getAllShows());

		return "show-timings";
	}

	// ================= ADMIN =================

	@GetMapping("/admin/shows")
	public String adminShowList(Model model) {
		model.addAttribute("shows", showService.getAllShows());
		return "admin/admin-show";
	}

	@GetMapping("/admin/shows/add")
	public String addShowForm(Model model) {
		model.addAttribute("show", new Show());
		model.addAttribute("movies", movieService.getAllMovies());
		model.addAttribute("theatres", theatreService.getAllTheatres());
		return "admin/admin-show-form";
	}

	@PostMapping("/admin/shows/add")
	public String addShow(@ModelAttribute Show show) {
		showService.addShow(show);
		return "redirect:/admin/shows";
	}

	@GetMapping("/admin/shows/edit/{id}")
	public String editShow(@PathVariable Long id, Model model) {
		model.addAttribute("show", showService.getShowById(id));
		model.addAttribute("movies", movieService.getAllMovies());
		model.addAttribute("theatres", theatreService.getAllTheatres());
		return "admin/admin-show-form";
	}

	@PostMapping("/admin/shows/update/{id}")
	public String updateShow(@PathVariable Long id, @ModelAttribute Show show) {
		showService.updateShow(id, show);
		return "redirect:/admin/shows";
	}

	@GetMapping("/admin/shows/delete/{id}")
	public String deleteShow(@PathVariable Long id) {
		showService.deleteShow(id);
		return "redirect:/admin/shows";
	}
}
