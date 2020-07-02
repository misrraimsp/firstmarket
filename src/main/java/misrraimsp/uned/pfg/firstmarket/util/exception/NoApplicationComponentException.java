package misrraimsp.uned.pfg.firstmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoApplicationComponentException extends RuntimeException {

    public NoApplicationComponentException(String message) {
        super(message);
    }

}