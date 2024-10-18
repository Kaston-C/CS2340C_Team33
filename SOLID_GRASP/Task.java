public class Task {
    protected String title;
    protected String description;
    protected String dueDate;
    protected String status;
    protected int priority;

    public Task (String title, String description, String dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }
}