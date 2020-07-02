package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.ItemRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.core.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ItemServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ItemRepository itemRepository;
    private final BookServer bookServer;

    @Autowired
    public ItemServer(ItemRepository itemRepository,
                      BookServer bookServer) {

        this.itemRepository = itemRepository;
        this.bookServer = bookServer;
    }

    public Item persist(Item item) {
        return itemRepository.save(item);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundByIdException(itemId, Item.class.getSimpleName()));
    }

    public Item create(Long bookId) throws EntityNotFoundByIdException {
        Item item = new Item();
        item.setBook(bookServer.findById(bookId));
        item.setQuantity(1);
        Item savedItem = itemRepository.save(item);
        LOGGER.debug("Item(id={}) created (bookId={})", savedItem.getId(), bookId);
        return savedItem;
    }

    public Item copy(Item item) {
        Item copyItem = new Item();
        copyItem.setBook(item.getBook());
        copyItem.setQuantity(item.getQuantity());
        copyItem = itemRepository.save(copyItem);
        LOGGER.debug("Item(id={}) copied from item(id={})", copyItem.getId(), item.getId());
        return copyItem;
    }

    public Set<Item> copy(Set<Item> items) {
        Set<Item> newItems = new HashSet<>();
        items.forEach(item -> newItems.add(this.copy(item)));
        return newItems;
    }

    public void increment(Long id) throws EntityNotFoundByIdException {
        Item item = this.findById(id);
        item.setQuantity(1 + item.getQuantity());
        itemRepository.save(item);
        LOGGER.debug("Item(id={}) incremented (new quantity = {})", item.getId(), item.getQuantity());
    }

    public void decrement(Long id) throws EntityNotFoundByIdException {
        Item item = this.findById(id);
        item.setQuantity(item.getQuantity() - 1);
        itemRepository.save(item);
        LOGGER.debug("Item(id={}) decremented (new quantity = {})", item.getId(), item.getQuantity());
    }

    public void delete(Item item) {
        itemRepository.delete(item);
        LOGGER.debug("Item(id={}) deleted", item.getId());
    }
}
