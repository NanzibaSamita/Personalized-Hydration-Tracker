import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    private String username;
    private String password;
    private int age;
    private double weight;
    private String activityLevel;
    private double dailyWaterGoal;

    public User(String username, String password, int age, double weight, String activityLevel) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.dailyWaterGoal = calculateDailyWaterGoal();
    }

    private double calculateDailyWaterGoal() {
        double baseWater = weight * 35; // Basic formula: Weight (kg) × 35
        if (activityLevel.equalsIgnoreCase("moderate")) {
            baseWater += 500; // Add 500ml for moderate activity
        } else if (activityLevel.equalsIgnoreCase("intense")) {
            baseWater += 1000; // Add 1000ml for intense activity
        }
        return baseWater;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getDailyWaterGoal() { return dailyWaterGoal; }
}