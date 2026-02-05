package com.cg;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.service.UserService;
@SpringBootTest
public class UserServiceTest {
	@Autowired
    private UserService userService;

    @BeforeAll
    public static void init() {
        System.out.println("User Test Cases");
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
        assertEquals("ADMIN", admin.getRole().name());
    }


    @Test
       public void testValidLogin() {
           User user = new User("kiran", "1234", Role.USER);
           userService.registerUser(user);

           User loggedIn = userService.login("kiran", "1234");
           assertNotNull(loggedIn);
           // Optional stronger checks:
           assertEquals("kiran", loggedIn.getUsername());
           assertEquals(Role.USER, loggedIn.getRole());
       }


    @Test
    public void testInvalidLogin() {
        User loggedIn = userService.login("wrong", "wrong");
        assertNull(loggedIn);
    }
}