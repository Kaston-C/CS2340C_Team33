public class Customer {
    private String name;
    private String email;

    public Customer( String name, String email){
        this.email = email;
        this.name = name;

    }
    public String getName() {
        return name;
    }

    public void setName(String new_name) {
        this.name = new_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String new_email) {
        this.email = new_email;
    }
}
