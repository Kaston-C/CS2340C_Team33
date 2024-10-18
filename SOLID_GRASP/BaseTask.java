import java.util.Date;

public class BaseTask implements Task {
    protected String title;
    protected String description;
    protected Date dueDate;
    protected String status;
    protected int priority;

    public BaseTask(String title, String description, Date dueDate, String status, int priority) {
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
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static class HighPriorityTask extends BaseTask {
        private String escalationContact;

        public HighPriorityTask(String title, String description, Date dueDate, String status, int priority, String escalationContact) {
            super(title, description, dueDate, status, priority);
            this.escalationContact = escalationContact;
        }

        public void setEscalationContact(String escalationContact) {
            this.escalationContact = escalationContact;
        }
    }
}
