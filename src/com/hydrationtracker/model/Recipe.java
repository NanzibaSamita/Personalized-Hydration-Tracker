package com.hydrationtracker.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private final String name;
    private final List<String> ingredients;
    private final String instructions;
    private final int preparationTime; // in minutes
    private final DifficultyLevel difficulty;

    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }

    public Recipe(String name, List<String> ingredients, String instructions,
                  int preparationTime, DifficultyLevel difficulty) {
        validateInputs(name, ingredients, instructions, preparationTime, difficulty);

        this.name = name;
        this.ingredients = new ArrayList<>(ingredients); // Defensive copy
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
    }

    private void validateInputs(String name, List<String> ingredients, String instructions,
                                int preparationTime, DifficultyLevel difficulty) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be empty");
        }
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Recipe must have at least one ingredient");
        }
        if (instructions == null || instructions.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructions cannot be empty");
        }
        if (preparationTime <= 0) {
            throw new IllegalArgumentException("Preparation time must be positive");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty level must be specified");
        }
    }

    // Getters (no setters to maintain immutability)
    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public String getInstructions() {
        return instructions;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    // Utility methods
    public boolean containsIngredient(String ingredient) {
        return ingredients.stream()
                .anyMatch(i -> i.equalsIgnoreCase(ingredient));
    }

    // Override equals, hashCode, and toString for better object handling
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return preparationTime == recipe.preparationTime &&
                name.equals(recipe.name) &&
                ingredients.equals(recipe.ingredients) &&
                instructions.equals(recipe.instructions) &&
                difficulty == recipe.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, instructions, preparationTime, difficulty);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", preparationTime=" + preparationTime +
                ", difficulty=" + difficulty +
                '}';
    }
}