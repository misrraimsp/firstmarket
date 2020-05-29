package misrraimsp.uned.pfg.firstmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IdNotFoundException extends IllegalArgumentException {

    public IdNotFoundException(String message) {
        super(message);
    }
}
