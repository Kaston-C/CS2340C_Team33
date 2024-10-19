import java.util.Date;

public class HighPriorityTask extends Task {
    private String escalationContact;

    public HighPriorityTask(String title, String description, Date dueDate, String status, int priority, String escalationContact) {
        super(title, description, dueDate, status, priority);
        this.escalationContact = escalationContact;
    }

    public void setEscalationContact(String escalationContact) {
        this.escalationContact = escalationContact;
    }
}