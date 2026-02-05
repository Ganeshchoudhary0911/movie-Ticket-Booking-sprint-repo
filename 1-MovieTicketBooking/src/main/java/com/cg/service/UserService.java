package com.cg.service;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User saveUser(User user) { return userRepository.save(user); }

    @Override
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }

    @Override
    public Optional<User> findByUsername(String username) { return userRepository.findByUsername(username); }
}