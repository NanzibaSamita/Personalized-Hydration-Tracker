import java.util.ArrayList;
import java.util.List;

public class HydrationTracker {
    private User user;
    private List<Double> dailyIntakeLog;
    private int streak;

    public HydrationTracker(User user) {
        this.user = user;
        this.dailyIntakeLog = new ArrayList<>();
        this.streak = 0;
    }

    public void logWaterIntake(double amount) {
        dailyIntakeLog.add(amount);
        if (getTotalIntake() >= user.getDailyWaterGoal()) {
            streak++;
        } else {
            streak = 0;
        }
    }

    public double getTotalIntake() {
        return dailyIntakeLog.stream().mapToDouble(Double::doubleValue).sum();
    }

    public int getStreak() {
        return streak;
    }

    public void checkDehydrationWarning() {
        if (getTotalIntake() < user.getDailyWaterGoal() * 0.5) {
            System.out.println("Warning: You are significantly below your daily water goal!");
        }
    }
}