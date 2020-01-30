package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ItemRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServer {

    private ItemRepository itemRepository;

    @Autowired
    public ItemServer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item persist(Item item) {
        return itemRepository.save(item);
    }

    public Item create(Book book) {
        Item item = new Item();
        item.setBook(book);
        item.setQuantity(1);
        return this.persist(item);
    }
}
