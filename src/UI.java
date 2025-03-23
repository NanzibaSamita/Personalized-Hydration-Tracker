import java.util.Scanner;

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
    }

    private void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManager.loginUser(username, password)) {
            System.out.println("Login successful!");
            User user = new User(username, password, 0, 0, ""); // Placeholder
            hydrationTracker = new HydrationTracker(user);
            showMainMenu();
        } else {
            System.out.println("Invalid username or password.");
            start();
        }
    }

    private void showMainMenu() {
        while (true) { // Keep showing the menu until the user logs out
            System.out.println("1. Log Water Intake");
            System.out.println("2. View Recipes");
            System.out.println("3. Add Recipe");
            System.out.println("4. Check Progress");
            System.out.println("5. View Daily Water Goal");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    logWaterIntake();
                    break;
                case 2:
                    recipeManager.displayRecipes();
                    break; // Return to the menu after displaying recipes
                case 3:
                    addRecipe();
                    break;
                case 4:
                    checkProgress();
                    break;
                case 5:
                    viewDailyWaterGoal();
                    break;
                case 6:
                    userManager.logoutUser();
                    start(); // Return to the login/register screen
                    return; // Exit the loop and method
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addRecipe() {
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ingredients: ");
        String ingredients = scanner.nextLine();
        System.out.print("Enter instructions: ");
        String instructions = scanner.nextLine();

        recipeManager.addRecipe(name, ingredients, instructions);
    }

    private void viewDailyWaterGoal() {
        User user = userManager.getCurrentUser(); // Assuming you have a method to get the current user
        if (user != null) {
            System.out.println("Your daily water intake goal is: " + user.getDailyWaterGoal() + " ml");
        } else {
            System.out.println("No user logged in.");
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