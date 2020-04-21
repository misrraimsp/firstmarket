package misrraimsp.uned.pfg.firstmarket.exception;

public class BadImageException extends IllegalArgumentException {
    public BadImageException() {
        super("Trying to persist an image without id or data");
    }
}
