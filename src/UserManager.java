import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;
    private static final String USER_DATA_FILE = "users.dat";

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public void registerUser(String username, String password, int age, double weight, String activityLevel) {
        User user = new User(username, password, age, weight, activityLevel);
        users.put(username, user);
        saveUsers();
        System.out.println("Daily water intake goal: " + user.getDailyWaterGoal() + " ml");
    }

    private User currentUser;

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logoutUser() {
        System.out.println("Logged out successfully.");
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing user data found. Starting fresh.");
        }
    }
}