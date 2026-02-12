package com.cg.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Role-Based Security Tests (2 methods)
 * 
 * Tests:
 * 8) shouldAuthenticateUser_whenRoleMatches
 * 9) shouldDenyPutRequest_whenUserLacksAdminRole
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoleBasedSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("shouldAuthenticateUser_whenRoleMatches")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldAuthenticateUser_whenRoleMatches() throws Exception {
        // Arrange: Admin user is authenticated
        // Act & Assert: Admin can access admin endpoints
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/movies"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("shouldDenyPutRequest_whenUserLacksAdminRole")
    @WithMockUser(username = "user", roles = "USER")
    void shouldDenyPutRequest_whenUserLacksAdminRole() throws Exception {
        // Arrange: Regular user attempts PUT to admin route
        // Act & Assert: Expect 403 Forbidden (SecurityConfig requires ADMIN role for PUT)
        mockMvc.perform(put("/admin/movies/1")
                .with(csrf())
                .param("movieName", "Updated Movie"))
                .andExpect(status().isForbidden());
    }
}