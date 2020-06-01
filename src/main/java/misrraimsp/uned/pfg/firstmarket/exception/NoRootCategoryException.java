package misrraimsp.uned.pfg.firstmarket.exception;


public class NoRootCategoryException extends NoApplicationComponentException {

    public NoRootCategoryException() {
        super("There is no root category");
    }

}
