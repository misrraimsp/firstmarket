package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ItemRepository;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServer {

    private ItemRepository itemRepository;
    private BookServer bookServer;

    @Autowired
    public ItemServer(ItemRepository itemRepository,
                      BookServer bookServer) {

        this.itemRepository = itemRepository;
        this.bookServer = bookServer;
    }

    public Item persist(Item item) {
        return itemRepository.save(item);
    }

    public Item create(Long bookId) {
        Item item = new Item();
        item.setBook(bookServer.findById(bookId));
        item.setQuantity(1);
        return itemRepository.save(item);
    }

    public void increment(Long id) {
        Item item = itemRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + id));
        item.setQuantity(1 + item.getQuantity());
        itemRepository.save(item);
    }

    public void decrement(Long id) {
        Item item = itemRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + id));
        item.setQuantity(item.getQuantity() - 1);
        itemRepository.save(item);
    }

    public void delete(Item item) {
        itemRepository.delete(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + id));
    }
}
