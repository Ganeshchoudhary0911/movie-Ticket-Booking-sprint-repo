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

}