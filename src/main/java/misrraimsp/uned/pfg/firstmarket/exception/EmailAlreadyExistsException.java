package misrraimsp.uned.pfg.firstmarket.exception;

public class EmailAlreadyExistsException extends IllegalArgumentException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
