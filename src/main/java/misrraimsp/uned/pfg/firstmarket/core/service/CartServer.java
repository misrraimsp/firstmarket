package misrraimsp.uned.pfg.firstmarket.core.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.core.data.CartRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Cart;
import misrraimsp.uned.pfg.firstmarket.core.model.Item;
import misrraimsp.uned.pfg.firstmarket.core.model.Sale;
import misrraimsp.uned.pfg.firstmarket.core.model.User;
import misrraimsp.uned.pfg.firstmarket.util.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.util.exception.ItemsAvailabilityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CartRepository cartRepository;
    private final ItemServer itemServer;
    private final BookServer bookServer;
    private final SaleServer saleServer;

    @Autowired
    public CartServer(CartRepository cartRepository,
                      ItemServer itemServer,
                      BookServer bookServer,
                      SaleServer saleServer) {

        this.cartRepository = cartRepository;
        this.itemServer = itemServer;
        this.bookServer = bookServer;
        this.saleServer = saleServer;
    }

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void addBook(Cart cart, Long bookId) throws EntityNotFoundByIdException, StripeException {
        Set<Item> items = new HashSet<>(cart.getItems());
        List<Long> matchingItemIds =
                items
                        .stream()
                        .filter(item -> item.getBook().getId().equals(bookId))
                        .map(Item::getId)
                        .collect(Collectors.toList())
                ;
        if (matchingItemIds.size() > 1) {
            LOGGER.warn("Trying to add a book(id={}) that already is in several items(ids={}) within the cart(id={})",
                    bookId, matchingItemIds.toString(), cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        if (matchingItemIds.isEmpty()) {
            Item newItem = itemServer.create(bookId);
            items.add(newItem);
            cart.setItems(items);
            bookServer.incrementCartBookRegistry(bookId);
        }
        else {
            itemServer.increment(matchingItemIds.get(0));
        }
        cartRepository.save(cart);
        LOGGER.debug("Book(id={}) added into cart(id={})", bookId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void incrementItem(Cart cart, Long itemId) throws EntityNotFoundByIdException, StripeException {
        Item itemToIncrement = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(itemToIncrement)) {
            LOGGER.warn("Trying to increment an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        itemServer.increment(itemId);
        cartRepository.save(cart);
        LOGGER.debug("Item(id={}) incremented inside cart(id={})", itemId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void decrementItem(Cart cart, Long itemId) throws EntityNotFoundByIdException, StripeException {
        Item itemToDecrement = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(itemToDecrement)) {
            LOGGER.warn("Trying to decrement an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        if (itemToDecrement.getQuantity() > 1){
            itemServer.decrement(itemToDecrement.getId());
            cartRepository.save(cart);
            LOGGER.debug("Item(id={}) decremented inside cart(id={})", itemId, cart.getId());
        }
        else {
            bookServer.decrementCartBookRegistry(itemToDecrement.getBook().getId());
            items.remove(itemToDecrement);
            cart.setItems(items);
            cartRepository.save(cart);
            itemServer.delete(itemToDecrement);
            LOGGER.debug("Last book of item(id={}) removed inside cart(id={}). Item itself also removed", itemId, cart.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public void removeItem(Cart cart, Long itemId, boolean doUnCommit) throws EntityNotFoundByIdException, StripeException {
        //update cart
        Item deletingItem = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(deletingItem)) {
            LOGGER.warn("Trying to remove an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        if (doUnCommit) cart = this.unCommitCart(cart);
        bookServer.decrementCartBookRegistry(deletingItem.getBook().getId());
        items.remove(deletingItem);
        cart.setItems(items);
        cartRepository.save(cart);
        //delete item
        itemServer.delete(deletingItem);
        LOGGER.debug("Item(id={}) removed from cart(id={})", itemId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public Cart unCommitCart(@NonNull Cart cart) throws EntityNotFoundByIdException, StripeException {
        if (cart.isCommitted()) {
            bookServer.restoreStock(cart.getItems());
            Set<Sale> sales = new HashSet<>(cart.getSales());
            cart.setSales(new HashSet<>());
            String piId = cart.getStripePaymentIntentId();
            PaymentIntent.retrieve(piId).cancel();
            cart.setStripePaymentIntentId(null);
            cart.setStripeClientSecret(null);
            cart = this.persist(cart);
            saleServer.deleteAll(sales);
            LOGGER.debug("Cart(id={}) successfully un-committed (pi id={})", cart.getId(), piId);
            return cart;
        }
        return cart;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public Cart commitCart(@NonNull User user) throws EntityNotFoundByIdException, ItemsAvailabilityException, StripeException {
        Cart cart = user.getCart();
        if (!cart.isCommitted()) {
            bookServer.checkAvailabilityFor(cart.getItems());
            PaymentIntent paymentIntent = PaymentIntent.create(PaymentIntentCreateParams
                    .builder()
                    .setCurrency("eur")
                    .setAmount(cart.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                    .putMetadata("user-id", Long.toString(user.getId()))
                    .build()
            );
            bookServer.removeFromStock(cart.getItems());
            cart.setStripePaymentIntentId(paymentIntent.getId());
            cart.setStripeClientSecret(paymentIntent.getClientSecret());
            cart.setSales(saleServer.createAll(cart.getItems()));
            cart = this.persist(cart);
            LOGGER.debug("User(id={}) cart(id={}) successfully committed (pi id={})", user.getId(), cart.getId(), cart.getStripePaymentIntentId());
            return cart;
        }
        return cart;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void pruneCart(Cart cart, Set<Item> itemsDisabled) throws StripeException {
        LOGGER.debug("Cart(id={}) pruning started due to {} book/s disability", cart.getId(), itemsDisabled.size());
        for (Item item : itemsDisabled) {
            this.removeItem(cart, item.getId(), false);
        }
        LOGGER.debug("Cart(id={}) pruning ended", cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public void resetCart(Cart cart) throws StripeException {
        LOGGER.debug("Cart(id={}) reset started", cart.getId());
        Set<Item> items = new HashSet<>(cart.getItems());
        for (Item item : items) {
            this.removeItem(cart, item.getId(), false);
        }
        cart.setSales(new HashSet<>());
        cart.setStripePaymentIntentId(null);
        cart.setStripeClientSecret(null);
        this.persist(cart);
        LOGGER.debug("Cart(id={}) reset ended", cart.getId());
    }

    public Set<Cart> findAll() {
        return cartRepository.findAll();
    }
}
