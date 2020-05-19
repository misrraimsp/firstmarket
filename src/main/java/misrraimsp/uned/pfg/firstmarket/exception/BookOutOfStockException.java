package misrraimsp.uned.pfg.firstmarket.exception;

import misrraimsp.uned.pfg.firstmarket.model.Item;

public class BookOutOfStockException extends IllegalArgumentException {

    public BookOutOfStockException(Item item) {
        super("Trying to set book stock negative. " +
                "Item id = " + item.getId() + ". " +
                "Book id = " + item.getBook().getId() + ". " +
                "Quantity = " + item.getQuantity() + ".");
    }
}
