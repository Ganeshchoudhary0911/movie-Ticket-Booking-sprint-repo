package com.cg.service;

import java.util.Optional;

import com.cg.entity.User;

public interface IUserService {
    User saveUser(User user);
<<<<<<< HEAD
    User findByEmail(String email);
    User findByUsername(String username);
    public User registerUser(User user);
=======
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
>>>>>>> a948f28cc119208da9844bcfe3de64e490875643
}
