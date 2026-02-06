package com.cg.controller;

import com.cg.dto.TheatreDto;
import com.cg.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("theatres", theatreService.getTheatresByCity(city));   // List<TheatreDto>
        } else {
            model.addAttribute("theatres", theatreService.getAllTheatres());         // List<TheatreDto>
        }
        return "theatre-list";  // create theatre-list.html (bind to TheatreDto)
    }

    // ================== ADMIN VIEW ===================

    @GetMapping("/admin/theatres")
    public String adminList(Model model) {
        model.addAttribute("theatres", theatreService.getAllTheatres());             // List<TheatreDto>
        return "admin/admin-theatre";
    }

    @GetMapping("/admin/theatres/add")
    public String showAddForm(Model model) {
        model.addAttribute("theatre", new TheatreDto());                             // bind DTO in form
        return "admin/admin-theatre-form";
    }

    @PostMapping("/admin/theatres/add")
    public String addTheatre(@ModelAttribute("theatre") TheatreDto theatre) {
        theatreService.addTheatre(theatre);                                          // accepts TheatreDto
        return "redirect:/admin/theatres";
    }

    @GetMapping("/admin/theatres/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        TheatreDto dto = theatreService.getTheatreById(id);                          // returns TheatreDto
        if (dto == null) {
            return "redirect:/admin/theatres?error=theatre-not-found";
        }
        model.addAttribute("theatre", theatreService.getTheatreById(id));
        return "admin/admin-theatre-form";
    }

    @PostMapping("/admin/theatres/update/{id}")
    public String updateTheatre(@PathVariable Long id, @ModelAttribute("theatre") TheatreDto theatre) {
        theatreService.updateTheatre(id, theatre);                                   // accepts TheatreDto
        return "redirect:/admin/theatres";
    }

    @GetMapping("/admin/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return "redirect:/admin/theatres";
    }
}