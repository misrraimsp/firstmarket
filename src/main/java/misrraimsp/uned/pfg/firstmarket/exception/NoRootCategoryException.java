package misrraimsp.uned.pfg.firstmarket.exception;

public class NoRootCategoryException extends RuntimeException {

    public NoRootCategoryException() {
        super("There is no root category");
    }
}
