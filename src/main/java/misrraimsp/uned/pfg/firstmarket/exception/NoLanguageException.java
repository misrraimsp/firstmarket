package misrraimsp.uned.pfg.firstmarket.exception;


public class NoLanguageException extends NoApplicationComponentException {

    public NoLanguageException() {
        super("There is no language assignable to books");
    }

}