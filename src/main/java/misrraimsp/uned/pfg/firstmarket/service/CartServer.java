package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.CartRepository;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServer {

    private CartRepository cartRepository;

    @Autowired
    public CartServer(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }
}
