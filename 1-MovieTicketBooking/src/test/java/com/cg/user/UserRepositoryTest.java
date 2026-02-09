package com.cg.user;

import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User createUser(String id) {
        User u = new User();
        u.setUsername("user" + id);
        u.setEmail("user" + id + "@mail.com");
        u.setPassword("pass123");
        u.setRole(Role.USER);
        u.setEnabled(true);
        return u;
    }

    @Test
    void saveUser_success() {
        User saved = userRepository.save(createUser("1"));
        assertNotNull(saved.getUserId());
    }

    @Test
    void findByUsername_success() {
        userRepository.save(createUser("2"));
        assertTrue(userRepository.findByUsername("user2").isPresent());
    }

    @Test
    void existsByEmail_success() {
        userRepository.save(createUser("3"));
        assertTrue(userRepository.existsByEmail("user3@mail.com"));
    }

    @Test
    void deleteUser_success() {
        User saved = userRepository.save(createUser("4"));
        userRepository.delete(saved);
        assertFalse(userRepository.findById(saved.getUserId()).isPresent());
    }
}
