import java.util.Date;

public class Task implements TaskInterface {
    protected String title;
    protected String description;
    protected Date dueDate;
    protected String status;
    protected int priority;

    public Task(String title, String description, Date dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    @Override
    public void markAsComplete() {
        this.status = "Complete";
    }

    @Override
    public void markAsInProgress() {
        this.status = "In Progress";
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
