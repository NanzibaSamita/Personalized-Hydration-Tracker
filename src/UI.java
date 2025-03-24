import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.hydrationtracker.service.HydrationTracker;
import com.hydrationtracker.service.RecipeManager;
import com.hydrationtracker.service.UserManager;
import com.hydrationtracker.model.User;
import com.hydrationtracker.model.Recipe;



public class UI {
    private UserManager userManager;
    private HydrationTracker hydrationTracker;
    private RecipeManager recipeManager;
    private Scanner scanner;

    public UI() {
        userManager = new UserManager();
        recipeManager = new RecipeManager("recipes.json");
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Hydration Tracker App!");
        System.out.println("1. Register\n2. Login\n3. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void registerUser() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            System.out.print("Enter weight (kg): ");
            double weight = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter activity level (low/moderate/intense): ");
            String activityLevel = scanner.nextLine();

            userManager.registerUser(username, password, age, weight, activityLevel);
            System.out.println("Registration successful!");
            start();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            start(); // Restart the process
        }
    }

    private void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManager.loginUser(username, password)) {
            System.out.println("Login successful!");
            // Get the actual user from UserManager
            User user = userManager.getCurrentUser().orElseThrow();
            hydrationTracker = new HydrationTracker(user);
            showMainMenu();
        } else {
            System.out.println("Invalid username or password.");
            start();
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Log Water Intake");
            System.out.println("2. View Recipes");
            System.out.println("3. Add Recipe");
            System.out.println("4. Check Progress");
            System.out.println("5. View Daily Water Goal");
            System.out.println("6. Logout");

            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> logWaterIntake();
                case 2 -> displayRecipes();
                case 3 -> addRecipe();
                case 4 -> checkProgress();
                case 5 -> viewDailyWaterGoal();
                case 6 -> {
                    userManager.logoutUser();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addRecipe() {
        try {
            System.out.println("\nAdd New Recipe");
            String name = readNonEmptyInput("Enter recipe name: ");

            System.out.println("Enter ingredients (comma separated): ");
            List<String> ingredients = Arrays.asList(
                    readNonEmptyInput("").split("\\s*,\\s*")
            );

            String instructions = readNonEmptyInput("Enter instructions: ");
            int prepTime = readIntInput("Enter preparation time (minutes): ");

            System.out.println("Select difficulty level:");
            System.out.println("1. Easy\n2. Medium\n3. Hard");
            int difficultyChoice = readIntInput("Enter choice (1-3): ");

            Recipe.DifficultyLevel difficulty;
            switch (difficultyChoice) {
                case 1 -> difficulty = Recipe.DifficultyLevel.EASY;
                case 2 -> difficulty = Recipe.DifficultyLevel.MEDIUM;
                case 3 -> difficulty = Recipe.DifficultyLevel.HARD;
                default -> throw new IllegalArgumentException("Invalid difficulty level");
            }

            Recipe recipe = new Recipe(name, ingredients, instructions, prepTime, difficulty);
            recipeManager.addRecipe(recipe);
            System.out.println("Recipe added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayRecipes() {
        System.out.println("\nAvailable Recipes:");
        List<Recipe> recipes = recipeManager.displayRecipes();

        if (recipes.isEmpty()) {
            System.out.println("No recipes available or error loading recipes.");
            System.out.println("Try adding new recipes first.");
            return;
        }

        recipes.forEach(recipe -> {
            System.out.println("\n" + recipe.getName().toUpperCase());
            System.out.println("Difficulty: " + recipe.getDifficulty());
            System.out.println("Prep Time: " + recipe.getPreparationTime() + " mins");
            System.out.println("Ingredients:");
            recipe.getIngredients().forEach(ing -> System.out.println("- " + ing));
            System.out.println("Instructions: " + recipe.getInstructions());
            System.out.println("----------------------------------");
        });
    }

    private void viewDailyWaterGoal() {
        userManager.getCurrentUser().ifPresentOrElse(
                user -> System.out.printf("Your daily water intake goal is: %.2f ml%n",  // Fixed %m to %n
                        user.getDailyWaterGoal()),
                () -> System.out.println("No user logged in.")
        );
    }

    // Helper methods
    private String readNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void logWaterIntake() {
        System.out.print("Enter amount of water consumed (ml): ");
        double amount = scanner.nextDouble();
        hydrationTracker.logWaterIntake(amount);
        hydrationTracker.checkDehydrationWarning();
    }
    private void checkProgress() {
        System.out.println("Total intake: " + hydrationTracker.getTotalIntake() + " ml");
        System.out.println("Current streak: " + hydrationTracker.getStreak() + " days");
    }
}