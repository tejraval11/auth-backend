package com.example.demo.service;

import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    // In-memory storage for users
    private List<User> users = new ArrayList<>();

    // Add a new user
    public User addUser(String username, String password) {
        // Check if username already exists
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        // Create new user
        User newUser = new User();
        newUser.setId(UUID.randomUUID()); // generate unique ID
        newUser.setUsername(username);
        newUser.setPassword(password);

        users.add(newUser);
        return newUser;
    }

    // Get all users
    public List<User> getAllUsers() {
        return users;
    }

    // Find user by username
    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // Optional: delete user by ID
    public boolean deleteUser(UUID id) {
        return users.removeIf(u -> u.getId().equals(id));
    }
}
