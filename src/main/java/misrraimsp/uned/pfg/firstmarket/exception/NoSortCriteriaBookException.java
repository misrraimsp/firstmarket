package misrraimsp.uned.pfg.firstmarket.exception;

public class NoSortCriteriaBookException extends NoApplicationComponentException {

    public NoSortCriteriaBookException() {
        super("There is no sort criteria applicable to books");
    }
}
