package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.CartRepository;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void addBook(Cart cart, Long bookId) {
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
}
