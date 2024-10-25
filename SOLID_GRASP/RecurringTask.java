import java.util.Date;

public class RecurringTask extends Task {
    private int recurrencePattern; // in days

    public RecurringTask(String title, String description, Date dueDate, String status, int priority, int recurrencePattern) {
        super(title, description, dueDate, status, priority);
        this.recurrencePattern = recurrencePattern;
    }

    public void setRecurrencePattern(int recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
}
