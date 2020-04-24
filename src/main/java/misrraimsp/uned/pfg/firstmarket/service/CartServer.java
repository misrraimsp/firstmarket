package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.CartRepository;
import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.ItemNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServer {

    private CartRepository cartRepository;
    private ItemServer itemServer;

    @Autowired
    public CartServer(CartRepository cartRepository,
                      ItemServer itemServer) {

        this.cartRepository = cartRepository;
        this.itemServer = itemServer;
    }

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public void addBook(Cart cart, Long bookId) throws BookNotFoundException, ItemNotFoundException {
        List<Item> items = cart.getItems();
        //check if item already exist on user cart
        boolean needsCreation = true;
        for (Item i : items) {
            if (i.getBook().getId().equals(bookId)) {
                needsCreation = false;
                //update and persist item
                itemServer.increment(i.getId());
                break;
            }
        }
        if(needsCreation){
            //create and persist item
            Item newItem = itemServer.create(bookId);
            //update cart
            items.add(newItem);
            cart.setItems(items);
        }
        //update and persist cart
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    public void incrementItem(Cart cart, Long itemId) throws ItemNotFoundException {
        itemServer.increment(itemId);
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
    }

    /**
     * coded for nothings happens if itemId do not match any item.id
     * by means of putting "cart.setLastModified(LocalDateTime.now());"
     * in appropriate position
     */
    @Transactional
    public void decrementItem(Cart cart, Long itemId) throws ItemNotFoundException {
        List<Item> items = cart.getItems();
        Item deletingItem = null;
        for (Item i : items) {
            if (i.getId().equals(itemId)) {
                //check item deletion condition
                if (i.getQuantity() > 1){//simple decrement item
                    itemServer.decrement(i.getId());
                    cart.setLastModified(LocalDateTime.now());
                }
                else {//delete item
                    deletingItem = i;
                    itemServer.delete(i);
                }
                break;
            }
        }
        if(deletingItem != null){
            //update cart
            items.remove(deletingItem);
            cart.setItems(items);
            cart.setLastModified(LocalDateTime.now());
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItem(Cart cart, Long itemId) throws ItemNotFoundException {
        //update cart
        Item deletingItem = itemServer.findById(itemId);
        List<Item> items = cart.getItems();
        items.remove(deletingItem);
        cart.setItems(items);
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
        //delete item
        itemServer.delete(deletingItem);
    }

    public List<Item> emptyCart(Cart cart) {
        List<Item> items = cart.getItems();
        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);
        return items;
    }

    public double getTotalPrice(Cart cart){
        double sum = 0;
        for (Item item : cart.getItems()){
            sum += item.getQuantity() * item.getBook().getPrice().doubleValue();
        }
        return sum;
    }
}
