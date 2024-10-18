import java.util.Date;
public class RecurringTask extends BaseTask {
    private String recurrencePattern;

    public RecurringTask(String title, String description, Date dueDate, String status, int priority, String recurrencePattern) {
        super(title, description, dueDate, status, priority);
        this.recurrencePattern = recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
}
