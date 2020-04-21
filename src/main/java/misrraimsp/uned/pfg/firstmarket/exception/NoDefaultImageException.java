package misrraimsp.uned.pfg.firstmarket.exception;

public class NoDefaultImageException extends RuntimeException {
    public NoDefaultImageException() {
        super("There is no default image");
    }
}
