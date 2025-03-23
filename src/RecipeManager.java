import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

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

    public void displayRecipes() {
        if (recipes.isEmpty()) {
            System.out.println("No recipes available.");
            return;
        }

        for (Object recipe : recipes) {
            JSONObject r = (JSONObject) recipe;
            System.out.println("Name: " + r.get("name"));
            System.out.println("Ingredients: " + r.get("ingredients"));
            System.out.println("Instructions: " + r.get("instructions"));
            System.out.println();
        }
    }

    public void addRecipe(String name, String ingredients, String instructions) {
        JSONObject newRecipe = new JSONObject();
        newRecipe.put("name", name);
        newRecipe.put("ingredients", ingredients);
        newRecipe.put("instructions", instructions);

        recipes.add(newRecipe);
        saveRecipes();
        System.out.println("Recipe added successfully!");
    }

    private void saveRecipes() {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(recipes.toJSONString());
            file.flush();
        } catch (Exception e) {
            System.out.println("Failed to save recipes: " + e.getMessage());
        }
    }
}