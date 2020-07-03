package misrraimsp.uned.pfg.firstmarket.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadImageException extends IllegalArgumentException {

    public BadImageException(String message) {
        super(message);
    }

    public BadImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
