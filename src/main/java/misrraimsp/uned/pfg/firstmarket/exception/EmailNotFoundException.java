package misrraimsp.uned.pfg.firstmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String email) {
        super("There is no such an email address: " + email);
    }
}
