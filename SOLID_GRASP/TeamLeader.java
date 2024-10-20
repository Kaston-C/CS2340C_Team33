public class TeamLeader extends TeamMember implements Specialization {
    private String role;

    public TeamLeader(String name, String email) {
        super(name, email);
    }

    @Override
    public void assignRole(String role) {
        this.role = role;
    }

    @Override
    public void removeRole() {
        this.role = null;
    }
}