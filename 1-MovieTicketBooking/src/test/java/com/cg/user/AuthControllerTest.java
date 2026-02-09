package com.cg.user;

import com.cg.controller.AuthController;
import com.cg.repository.UserRepository;

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
    private UserRepository userRepo;

    @MockBean
    private PasswordEncoder encoder;

    // ======================
    @Test
    void loginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // ======================
    @Test
    void signupPage_shouldReturnSignupView() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"));
    }

    // ======================
    
//    @Test
//    void doSignup_success_shouldRedirectLogin() throws Exception {
//
//        when(userRepo.existsByUsername(any())).thenReturn(false);
//        when(userRepo.existsByEmail(any())).thenReturn(false);
//        when(encoder.encode(any())).thenReturn("encoded");
//
//        mockMvc.perform(post("/signup")
//                .param("username", "alice123")
//                .param("email", "alice@test.com")
//                .param("password", "password123")
//                .param("phoneNumber", "9999999999"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login?signup=true"));
//
//        verify(userRepo).save(any(User.class));
//    }


    // ======================
    @Test
    void doSignup_usernameExists_shouldReturnSignupView() throws Exception {

        when(userRepo.existsByUsername("alice")).thenReturn(true);

        mockMvc.perform(post("/signup")
                .param("user.username", "alice")
                .param("user.email", "a@test.com")
                .param("user.password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    // ======================
    @Test
    void doSignup_emailExists_shouldReturnSignupView() throws Exception {

        when(userRepo.existsByUsername(any())).thenReturn(false);
        when(userRepo.existsByEmail("a@test.com")).thenReturn(true);

        mockMvc.perform(post("/signup")
                .param("user.username", "alice")
                .param("user.email", "a@test.com")
                .param("user.password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }
}
