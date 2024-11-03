import java.util.logging.Logger;

public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    public void sendEmail(Customer customer, String subject, String message) {
        if (customer == null || subject == null || message == null) {
            logger.severe("Customer, subject, and message must not be null");
            return;
        }

        logger.info("Email to: " + customer.getEmail());
        logger.info("Subject: " + subject);
        logger.info("Body: " + message);
    }
}
