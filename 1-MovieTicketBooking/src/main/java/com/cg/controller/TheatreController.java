package com.cg.controller;

import com.cg.dto.TheatreDto;
import com.cg.service.TheatreService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TheatreController {

	@Autowired
	private TheatreService theatreService;

	public TheatreController(TheatreService theatreService) {
		this.theatreService = theatreService;
	}

	// ================== USER VIEW ===================

	@GetMapping("/theatres")
	public String viewTheatresByCity(@RequestParam(required = false) String city, Model model) {
		if (city != null && !city.isBlank()) {
			model.addAttribute("theatres", theatreService.getTheatresByCity(city));
		} else {
			model.addAttribute("theatres", theatreService.getAllTheatres());
		}
		return "theatre-list";
	}

	// ================== ADMIN VIEW ===================

	@GetMapping("/admin/theatres")
	public String adminList(Model model) {
		model.addAttribute("theatres", theatreService.getAllTheatres());
		return "admin/admin-theatre";
	}

	@GetMapping("/admin/theatres/add")
	public String showAddForm(Model model) {
		model.addAttribute("theatre", new TheatreDto());
		return "admin/admin-theatre-form";
	}

	// CREATE (POST)
	@PostMapping("/admin/theatres")
	public String addTheatre(@Valid @ModelAttribute("theatre") TheatreDto theatre,
	                         BindingResult bindingResult,
	                         Model model, RedirectAttributes ra) {
	    if (bindingResult.hasErrors()) {
	        return "admin/admin-theatre-form";
	    }
	    theatreService.addTheatre(theatre);
	    ra.addFlashAttribute("success", "Theatre created successfully");
	    return "redirect:/admin/theatres";
	}

	@GetMapping("/admin/theatres/edit/{id}")
	public String editPage(@PathVariable Long id, Model model) {
		TheatreDto dto = theatreService.getTheatreById(id);
		if (dto == null) {
			return "redirect:/admin/theatres?error=theatre-not-found";
		}
		model.addAttribute("theatre", dto);
		return "admin/admin-theatre-form";
	}

	// UPDATE (PUT)
	@PutMapping("/admin/theatres/{id}")
	public String updateTheatre(@PathVariable Long id,
	                            @Valid @ModelAttribute("theatre") TheatreDto theatre,
	                            BindingResult bindingResult,
	                            Model model, RedirectAttributes ra) {

	    if (bindingResult.hasErrors()) {
	        // ensure the id stays in the model if your form action uses it
	        model.addAttribute("theatre", theatre);
	        return "admin/admin-theatre-form";
	    }

	    theatreService.updateTheatre(id, theatre);
	    ra.addFlashAttribute("success", "Theatre updated successfully");
	    return "redirect:/admin/theatres";
	}

	// DELETE (DELETE)
	@DeleteMapping("/admin/theatres/{id}")
	public String deleteTheatre(@PathVariable Long id) {
		theatreService.deleteTheatre(id);
		return "redirect:/admin/theatres";
	}
}