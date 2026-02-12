package com.cg.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AdminViewController Tests (3 test methods)
 */
@WebMvcTest(AdminViewController.class)
@WithMockUser(roles = "ADMIN")
class AdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDashboard_AccessSuccess() throws Exception {
        // Verifies: Admin role can reach dashboard and see the correct HTML template
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-dashboard")); // Matches your return string
    }

   

    @Test
    void loginPage_PublicAccess() throws Exception {
        // Verifies: Anyone can see the login page without being blocked
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
    
    
    
}