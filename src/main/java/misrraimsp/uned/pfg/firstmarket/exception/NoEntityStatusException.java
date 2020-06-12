package misrraimsp.uned.pfg.firstmarket.exception;


public class NoEntityStatusException extends NoApplicationComponentException {

    public NoEntityStatusException(String entity) {
        super("There is no status assignable to " + entity);
    }

}