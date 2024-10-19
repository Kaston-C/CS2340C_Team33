public class TeamMember {
    private String name;
    private String email;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void joinProject(Project project) {
        project.addTeamMember(this);
    }

    public void leaveProject(Project project) {
        project.removeTeamMember(this);
    }
}