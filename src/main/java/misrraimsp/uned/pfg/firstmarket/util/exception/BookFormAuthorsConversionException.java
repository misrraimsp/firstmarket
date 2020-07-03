package misrraimsp.uned.pfg.firstmarket.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BookFormAuthorsConversionException extends IllegalArgumentException {
    public BookFormAuthorsConversionException(String s) {
        super(s);
    }
}
