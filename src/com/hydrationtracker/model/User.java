package com.hydrationtracker.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final int age;
    private final double weight;
    private final String activityLevel;
    private final double dailyWaterGoal;

    public User(String username, String password, int age, double weight, String activityLevel) {
        validateInputs(username, password, age, weight, activityLevel);
        this.username = username;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.activityLevel = activityLevel.toLowerCase();
        this.dailyWaterGoal = calculateDailyWaterGoal();
    }

    private void validateInputs(String username, String password, int age, double weight, String activityLevel) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        if (!activityLevel.equalsIgnoreCase("low") &&
                !activityLevel.equalsIgnoreCase("moderate") &&
                !activityLevel.equalsIgnoreCase("intense")) {
            throw new IllegalArgumentException("Invalid activity level");
        }
    }

    private double calculateDailyWaterGoal() {
        double baseWater = weight * 35;
        switch (activityLevel) {
            case "moderate":
                return baseWater + 500;
            case "intense":
                return baseWater + 1000;
            default:
                return baseWater;
        }
    }

    // Getters - no setters to maintain immutability
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getDailyWaterGoal() {
        return this.dailyWaterGoal;
    }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public String getActivityLevel() { return activityLevel; }
}