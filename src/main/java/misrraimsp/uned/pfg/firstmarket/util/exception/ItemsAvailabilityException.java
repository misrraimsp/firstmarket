package misrraimsp.uned.pfg.firstmarket.exception;

import misrraimsp.uned.pfg.firstmarket.core.model.Item;

import java.util.Set;

public class ItemsAvailabilityException extends IllegalArgumentException {

    Set<Item> itemsOutOfStock;
    Set<Item> itemsDisabled;

    public ItemsAvailabilityException(Set<Item> itemsOutOfStock, Set<Item> itemsDisabled) {
        super("Trying to checkout with items that are no available");
        this.itemsOutOfStock = itemsOutOfStock;
        this.itemsDisabled = itemsDisabled;
    }

    public Set<Item> getItemsOutOfStock() {
        return itemsOutOfStock;
    }

    public Set<Item> getItemsDisabled() {
        return itemsDisabled;
    }
}
