package com.cg.controller;

import com.cg.entity.Theatre;
import com.cg.service.TheatreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TheatreController {

	@Autowired
    TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    // ================== USER VIEW ===================

    @GetMapping("/theatres")
    public String viewTheatresByCity(@RequestParam(required = false) String city, Model model) {

        if (city != null) {
            model.addAttribute("theatres", theatreService.getTheatresByCity(city));
        } else {
            model.addAttribute("theatres", theatreService.getAllTheatres());
        }

        return "theatre-list";  // create theatre-list.html
    }

    // ================== ADMIN VIEW ===================

    @GetMapping("/admin/theatres")
    public String adminList(Model model) {
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/admin-theatre";
    }

    @GetMapping("/admin/theatres/add")
    public String showAddForm(Model model) {
        model.addAttribute("theatre", new Theatre());
        return "admin/admin-theatre-form";
    }

    @PostMapping("/admin/theatres/add")
    public String addTheatre(@ModelAttribute Theatre theatre) {
        theatreService.addTheatre(theatre);
        return "redirect:/admin/theatres";
    }

    @GetMapping("/admin/theatres/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("theatre", theatreService.getTheatreById(id));
        return "admin/admin-theatre-form";
    }

    @PostMapping("/admin/theatres/update/{id}")
    public String updateTheatre(@PathVariable Long id, @ModelAttribute Theatre theatre) {
        theatreService.updateTheatre(id, theatre);
        return "redirect:/admin/theatres";
    }

    @GetMapping("/admin/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return "redirect:/admin/theatres";
    }
}