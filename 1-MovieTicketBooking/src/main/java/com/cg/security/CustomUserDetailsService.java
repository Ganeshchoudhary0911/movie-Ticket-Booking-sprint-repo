package com.cg.security;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Try by username, then email — supports both login styles
        User user = userRepo.findByUsername(username)
                .or(() -> userRepo.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build your Spring Security UserDetails here from your entity
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // or use email as username if that’s your primary
                .password(user.getPassword())
                .authorities(user.getRole().name()) // adjust to your authorities/roles mapping
                .accountLocked(!user.isEnabled())   // adjust to your flags
                .build();
    }
}
