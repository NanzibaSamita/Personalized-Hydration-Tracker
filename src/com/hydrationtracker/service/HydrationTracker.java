package com.hydrationtracker.service;

import java.util.ArrayList;
import java.util.List;

import com.hydrationtracker.model.User;

public class HydrationTracker {
    private static final double DEHYDRATION_THRESHOLD = 0.5;

    private final User user;
    private final List<Double> dailyIntakeLog;
    private int streak;

    public HydrationTracker(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
        this.dailyIntakeLog = new ArrayList<>();
        this.streak = 0;
    }

    public void logWaterIntake(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Water intake amount must be positive");
        }
        dailyIntakeLog.add(amount);
        streak = (getTotalIntake() >= user.getDailyWaterGoal()) ? streak + 1 : 0;
    }

    public double getTotalIntake() {
        return dailyIntakeLog.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public int getStreak() {
        return streak;
    }

    public void checkDehydrationWarning() {
        if (getTotalIntake() < user.getDailyWaterGoal() * DEHYDRATION_THRESHOLD) {
            System.out.println("Warning: You are significantly below your daily water goal!");
        }
    }
}