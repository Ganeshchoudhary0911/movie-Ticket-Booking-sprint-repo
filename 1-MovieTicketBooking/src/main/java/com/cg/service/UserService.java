package com.cg.service;
 
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class UserService implements IUserService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
 
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
 