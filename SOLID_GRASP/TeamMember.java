public class TeamMember {
    private String name;
    private String email;
    private String role;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void assignRole(String role) {
        this.role = role;
    }

    public void removeRole() {
        this.role = null;
    }
}