package com.cg;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.service.UserService;

public class UserServiceTest {

    static UserService userService;

    @BeforeAll
    public static void init() {
        userService = new UserService();
    }

    @Test
    public void testUserRegistration() {
        User user = new User("anshu", "pass123", Role.USER);
        User saved = userService.registerUser(user);

        assertNotNull(saved);
        assertEquals("anshu", saved.getUsername());
    }

    @Test
    public void testAdminRoleAssignment() {
        User admin = new User("admin", "admin@123", Role.ADMIN);
        assertEquals("ADMIN", admin.getRole());
    }

    @Test
    public void testValidLogin() {
        User user = new User("kiran", "1234", Role.USER);
        userService.registerUser(user);

        User loggedIn = userService.login("kiran", "1234");
        assertNotNull(loggedIn);
    }

    @Test
    public void testInvalidLogin() {
        User loggedIn = userService.login("wrong", "wrong");
        assertNull(loggedIn);
    }
}