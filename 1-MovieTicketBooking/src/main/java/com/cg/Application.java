package com.cg;
 
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.cg.entity.Role;

import com.cg.entity.User;

import com.cg.repository.UserRepository;
 
@SpringBootApplication

public class Application {
 
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}
 
	@Bean

    CommandLineRunner seedAdmin(UserRepository userRepo, PasswordEncoder encoder) {

        return args -> {

            if (!userRepo.existsByUsername("admin")) {

                User admin = new User();

                admin.setUsername("admin");

                admin.setEmail("admin@cinema.local");

                admin.setPassword(encoder.encode("admin@123")); // change in prod

                admin.setRole(Role.ADMIN);

                admin.setEnabled(true);

                userRepo.save(admin);

                System.out.println("Seeded default ADMIN (admin/admin@123)");

            }

        };

    }
}