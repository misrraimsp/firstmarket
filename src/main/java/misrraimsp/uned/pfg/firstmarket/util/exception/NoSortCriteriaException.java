package misrraimsp.uned.pfg.firstmarket.util.exception;

public class NoSortCriteriaException extends NoApplicationComponentException {

    public NoSortCriteriaException(String className) {
        super("There is no sort criteria applicable to class " + className);
    }
}
