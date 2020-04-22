package misrraimsp.uned.pfg.firstmarket.exception;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String email) {
        super("There is no such an email address: " + email);
    }
}
