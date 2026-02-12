package com.cg.user;

import com.cg.controller.AuthController;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;   

    // ---------------------

    @Test
    void loginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // ---------------------

    @Test
    void signupPage_shouldReturnSignupView() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"));
    }

    // ---------------------

    @Test
    void doSignup_usernameExists_shouldReturnSignupView() throws Exception {

        when(userService.existsByUsername("alice")).thenReturn(true);

        mockMvc.perform(post("/signup")
                .param("username", "alice")
                .param("email", "a@test.com")
                .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    // ---------------------

    @Test
    void doSignup_emailExists_shouldReturnSignupView() throws Exception {

        when(userService.existsByUsername(any())).thenReturn(false);
        when(userService.existsByEmail("a@test.com")).thenReturn(true);

        mockMvc.perform(post("/signup")
                .param("username", "alice")
                .param("email", "a@test.com")
                .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }
}
