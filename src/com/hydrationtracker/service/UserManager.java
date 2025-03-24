package com.hydrationtracker.service;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.hydrationtracker.model.User;

public class UserManager {
    private final Map<String, User> users;
    private static final String USER_DATA_FILE = "users.dat";
    private User currentUser;

    public UserManager() {
        this.users = new HashMap<>();
        loadUsers();
    }

    public void registerUser(String username, String password, int age, double weight, String activityLevel) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        try {
            User user = new User(username, password, age, weight, activityLevel);
            users.put(username, user);
            saveUsers();
            System.out.printf("Daily water intake goal: %.2f ml%n", user.getDailyWaterGoal());
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw for UI to handle
        }
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public void logoutUser() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Failed to save user data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            Object readObject = ois.readObject();
            if (readObject instanceof Map) {
                users.putAll((Map<String, User>) readObject);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing user data found. Starting fresh.");
        }
    }

}