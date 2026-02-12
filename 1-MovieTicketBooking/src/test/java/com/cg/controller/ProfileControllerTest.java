package com.cg.controller;

import com.cg.entity.User;
import com.cg.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

// ⭐ THESE STATIC IMPORTS ARE KEY FOR THE CODE TO WORK
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // ✅ 1. Positive: Can access page if logged in
    @Test
    void testAccessPage() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        when(userService.findEntityByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profile/edit").with(user("testuser"))) 
                .andExpect(status().isOk())
                .andExpect(view().name("profile-edit"));
    }

    // ❌ 2. Negative: Redirects if NOT logged in
    @Test
    void testNoLoginRedirect() throws Exception {
        mockMvc.perform(get("/profile/edit")) // No .with(user(...))
                .andExpect(status().is3xxRedirection());
    }

    // ❌ 3. Negative: Password too short error
    @Test
    void testPasswordError() throws Exception {
        User user = new User();
        when(userService.findEntityByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/profile/update")
                .with(user("testuser"))
                .param("password", "123")) // Only 3 chars
                .andExpect(flash().attribute("errorMessage", "Password must be at least 8 characters."))
                .andExpect(redirectedUrl("/profile/edit"));
    }
}
