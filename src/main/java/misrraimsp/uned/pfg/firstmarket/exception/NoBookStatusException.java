package misrraimsp.uned.pfg.firstmarket.exception;


public class NoBookStatusException extends NoApplicationComponentException {

    public NoBookStatusException() {
        super("There is no status assignable to books");
    }

}