package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.config.JwtUtil;
import com.example.Subsidy_Tracking_System.entity.User;
import com.example.Subsidy_Tracking_System.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String rawPassword) {
        Optional<User> result = userRepository.findByUsername(username);
        if (result.isEmpty()) {
            return null;
        }

        User user = result.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
    }
}