package misrraimsp.uned.pfg.firstmarket.exception;

public class ItemNotFoundException extends IdNotFoundException {

    public ItemNotFoundException(Long itemId) {
        super("There is no Item with id=" + itemId);
    }
}
