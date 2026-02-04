package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard"; // loads admin-dashboard.html
    }
}
