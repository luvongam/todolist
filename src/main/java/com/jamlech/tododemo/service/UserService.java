package com.jamlech.tododemo.service;

import com.jamlech.tododemo.entity.User;
import com.jamlech.tododemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public Optional<User> getUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }
    public boolean isAdmin(User user) {
        return user.getRole() == User.Role.ADMIN;
    }
}
