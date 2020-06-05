package misrraimsp.uned.pfg.firstmarket.exception;

public class NoSortCriteriaException extends NoApplicationComponentException {

    public NoSortCriteriaException(String className) {
        super("There is no sort criteria applicable to class " + className);
    }
}
