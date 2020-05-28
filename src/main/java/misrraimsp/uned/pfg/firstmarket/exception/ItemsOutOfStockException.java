package misrraimsp.uned.pfg.firstmarket.exception;

import misrraimsp.uned.pfg.firstmarket.model.Item;

import java.util.Set;

public class ItemsOutOfStockException extends IllegalArgumentException {

    private Set<Item> items;

    public ItemsOutOfStockException(Set<Item> items) {
        super("Trying to set book stock negative");
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
    }
}
