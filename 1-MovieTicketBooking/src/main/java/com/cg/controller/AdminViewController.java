package com.cg.controller;

import com.cg.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// NOTE: Replace with your service injections to load real data
@Controller
@RequestMapping("/admin")
public class AdminViewController {

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/admin-dashboard";
    }

    // ===================== MOVIES ======================
//    @GetMapping("/movies")
//    public String moviesPage() {
//        return "admin/admin-movie";     // admin-movie.html
//    }
//
//    @GetMapping("/movies/new")
//    public String addMoviePage() {
//        return "admin/admin-movie-form"; // admin-movie-form.html
//    }
//
//    @GetMapping("/movies/{id}/edit")
//    public String editMoviePage() {
//        return "admin/admin-movie-form"; // reuse same form page
//    }

    // ===================== THEATRES ======================
//    @GetMapping("/theatres")
//    public String theatresPage() {
//        return "admin/admin-theatre";     // admin-theatre.html
//    }

    @GetMapping("/theatres/new")
    public String addTheatrePage() {
        return "admin/admin-theatre-form"; // admin-theatre-form.html
    }

    @GetMapping("/theatres/{id}/edit")
    public String editTheatrePage() {
        return "admin/admin-theatre-form"; // reuse form page
    }

    @GetMapping("/theatres/list")
    public String theatreListPage() {
        return "admin/theatre-list";        // theatre-list.html
    }

    // ===================== SHOWS ======================
//    @GetMapping("/shows")
//    public String showsPage() {
//        return "admin/admin-show";          // admin-show.html
//    }
//
//    @GetMapping("/shows/new")
//    public String addShowPage() {
//        return "admin/admin-show-form";     // admin-show-form.html
//    }
//
//    @GetMapping("/shows/{id}/edit")
//    public String editShowPage() {
//        return "admin/admin-show-form";     // reuse form page
//    }
}