package com.cg.service;

import java.util.Optional;

import com.cg.entity.User;

public interface IUserService {
    User saveUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
