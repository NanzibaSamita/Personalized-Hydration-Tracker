package com.hydrationtracker.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hydrationtracker.model.Recipe;

public class RecipeManager {
    private JSONArray recipes;
    private String filePath;

    public RecipeManager(String filePath) {
        this.filePath = filePath;
        loadRecipes();
    }

    private void loadRecipes() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            recipes = (JSONArray) parser.parse(reader);
        } catch (Exception e) {
            System.out.println("Failed to load recipes. Starting with an empty list.");
            recipes = new JSONArray();
        }
    }


    public void addRecipe(Recipe recipe) {
        JSONObject newRecipe = new JSONObject();
        newRecipe.put("name", recipe.getName());

        JSONArray ingredients = new JSONArray();
        ingredients.addAll(recipe.getIngredients());
        newRecipe.put("ingredients", ingredients);

        newRecipe.put("instructions", recipe.getInstructions());
        newRecipe.put("preparationTime", recipe.getPreparationTime());
        newRecipe.put("difficulty", recipe.getDifficulty().toString());

        recipes.add(newRecipe);
        saveRecipes();
    }

    protected void saveRecipes() {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(recipes.toJSONString());
            file.flush();
        } catch (Exception e) {
            System.err.println("Failed to save recipes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Recipe> displayRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        for (Object recipe : recipes) {
            try {
                JSONObject r = (JSONObject) recipe;

                // Validate required fields
                if (r.get("name") == null || r.get("instructions") == null ||
                        r.get("preparationTime") == null || r.get("difficulty") == null) {
                    System.err.println("Skipping recipe - missing required fields");
                    continue;
                }

                // Handle ingredients
                List<String> ingredients = new ArrayList<>();
                Object ing = r.get("ingredients");
                if (ing instanceof JSONArray) {
                    List<String> finalIngredients = ingredients;
                    ((JSONArray)ing).forEach(item -> finalIngredients.add(item.toString()));
                } else if (ing instanceof String) {
                    ingredients = Arrays.asList(((String)ing).split("\\s*,\\s*"));
                }

                // Handle preparation time (Integer or Long)
                int prepTime;
                Object time = r.get("preparationTime");
                if (time instanceof Long) {
                    prepTime = ((Long)time).intValue();
                } else if (time instanceof Integer) {
                    prepTime = (Integer)time;
                } else {
                    throw new IllegalArgumentException("Invalid preparationTime format");
                }

                // Handle difficulty
                String difficultyStr = ((String)r.get("difficulty")).toUpperCase();
                Recipe.DifficultyLevel difficulty = Recipe.DifficultyLevel.valueOf(difficultyStr);

                recipeList.add(new Recipe(
                        (String)r.get("name"),
                        ingredients,
                        (String)r.get("instructions"),
                        prepTime,
                        difficulty
                ));
            } catch (Exception e) {
                System.err.println("Error loading recipe: " + e.getMessage());
            }
        }
        return recipeList;
    }

}