
public class EmailSender {
    public static void sendEmail(Customer customer, String subject, String message){
        System.out.println("Email to: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
