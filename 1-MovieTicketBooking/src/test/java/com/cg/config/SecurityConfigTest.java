package com.cg.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // --- POSITIVE TEST CASES ---

    @Test
  @DisplayName("shouldAllowPublicEndpoints_whenAccessedAnonymously")
  @WithAnonymousUser
  void shouldAllowPublicEndpoints_whenAccessedAnonymously() throws Exception {
      // Arrange & Act & Assert
      mockMvc.perform(get("/"))
              .andExpect(status().isOk());

      mockMvc.perform(get("/login"))
              .andExpect(status().isOk());

      mockMvc.perform(get("/signup"))
              .andExpect(status().isOk());
  }

   

    @Test
  @DisplayName("shouldReturnForbidden_whenUserWithoutRoleAccessesAdmin")
  @WithMockUser(roles = "USER")
  void shouldReturnForbidden_whenUserWithoutRoleAccessesAdmin() throws Exception {
      // Arrange: User with USER role attempts to access ADMIN endpoint
      // Act & Assert: Expect 403 Forbidden
      mockMvc.perform(get("/admin/dashboard"))
              .andExpect(status().isForbidden());

      mockMvc.perform(get("/admin/movies"))
              .andExpect(status().isForbidden());
  }

    // --- NEGATIVE TEST CASES ---

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAccessAdminRoutes() throws Exception {
        // Tests Role-based restriction (Expected: 403 Forbidden)
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedUserRedirectedToLogin() throws Exception {
        // Tests protection of sensitive routes
        mockMvc.perform(get("/profile/settings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginFailsWithInvalidCredentials() throws Exception {
        // Tests formLogin failure configuration
        mockMvc.perform(formLogin("/do-login")
                .user("wrongUser")
                .password("wrongPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}









































//package com.cg.config;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * SecurityConfig Integration Tests (2 methods)
// * 
// * Tests:
// * 1) shouldAllowPublicEndpoints_whenAccessedAnonymously
// * 2) shouldReturnForbidden_whenUserWithoutRoleAccessesAdmin
// */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class SecurityConfigTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @DisplayName("shouldAllowPublicEndpoints_whenAccessedAnonymously")
//    @WithAnonymousUser
//    void shouldAllowPublicEndpoints_whenAccessedAnonymously() throws Exception {
//        // Arrange & Act & Assert
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/signup"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("shouldReturnForbidden_whenUserWithoutRoleAccessesAdmin")
//    @WithMockUser(roles = "USER")
//    void shouldReturnForbidden_whenUserWithoutRoleAccessesAdmin() throws Exception {
//        // Arrange: User with USER role attempts to access ADMIN endpoint
//        // Act & Assert: Expect 403 Forbidden
//        mockMvc.perform(get("/admin/dashboard"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(get("/admin/movies"))
//                .andExpect(status().isForbidden());
//    }
//}