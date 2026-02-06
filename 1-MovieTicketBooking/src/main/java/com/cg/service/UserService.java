package com.cg.service;

import com.cg.config.*;
import com.cg.entity.*;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;

<<<<<<< HEAD
import jakarta.transaction.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
=======
import java.util.Optional;
>>>>>>> a948f28cc119208da9844bcfe3de64e490875643

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
	private final Map<String, User> store = new ConcurrentHashMap<>();

	@Autowired
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

<<<<<<< HEAD
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional
	public User registerUser(User user) {
		// Decide duplicate behavior; throw is often clearer for tests
		if (store.containsKey(user.getUsername())) {
			throw new IllegalArgumentException("User already exists: " + user.getUsername());
		}
		store.put(user.getUsername(), user);
		return user;
	}

	public User login(String username, String password) {
		User u = store.get(username);
		if (u == null)
			return null;
		return u.getPassword().equals(password) ? u : null;
	}

=======
    @Override
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }

    @Override
    public Optional<User> findByUsername(String username) { return userRepository.findByUsername(username); }
>>>>>>> a948f28cc119208da9844bcfe3de64e490875643
}