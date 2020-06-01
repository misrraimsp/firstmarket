package misrraimsp.uned.pfg.firstmarket.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.data.CartRepository;
import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.ItemNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.ItemsAvailabilityException;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private CartRepository cartRepository;
    private ItemServer itemServer;
    private BookServer bookServer;

    @Autowired
    public CartServer(CartRepository cartRepository,
                      ItemServer itemServer,
                      BookServer bookServer) {

        this.cartRepository = cartRepository;
        this.itemServer = itemServer;
        this.bookServer = bookServer;
    }

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void addBook(Cart cart, Long bookId) throws BookNotFoundException, ItemNotFoundException, StripeException {
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
        }
        else {
            itemServer.increment(matchingItemIds.get(0));
        }
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
        LOGGER.debug("Book(id={}) added into cart(id={})", bookId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void incrementItem(Cart cart, Long itemId) throws ItemNotFoundException, StripeException {
        Item itemToIncrement = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(itemToIncrement)) {
            LOGGER.warn("Trying to increment an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        itemServer.increment(itemId);
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
        LOGGER.debug("Item(id={}) incremented inside cart(id={})", itemId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void decrementItem(Cart cart, Long itemId) throws ItemNotFoundException, StripeException {
        Item itemToDecrement = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(itemToDecrement)) {
            LOGGER.warn("Trying to decrement an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        if (itemToDecrement.getQuantity() > 1){
            itemServer.decrement(itemToDecrement.getId());
            cart.setLastModified(LocalDateTime.now());
            cartRepository.save(cart);
            LOGGER.debug("Item(id={}) decremented inside cart(id={})", itemId, cart.getId());
        }
        else {
            items.remove(itemToDecrement);
            cart.setItems(items);
            cart.setLastModified(LocalDateTime.now());
            cartRepository.save(cart);
            itemServer.delete(itemToDecrement);
            LOGGER.debug("Last book of item(id={}) removed inside cart(id={}). Item itself also removed", itemId, cart.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = StripeException.class)
    public void removeItem(Cart cart, Long itemId) throws ItemNotFoundException, StripeException {
        //update cart
        Item deletingItem = itemServer.findById(itemId);
        Set<Item> items = new HashSet<>(cart.getItems());
        if (!items.contains(deletingItem)) {
            LOGGER.warn("Trying to remove an item(id={}) that is not present in the cart(id{})", itemId, cart.getId());
            return;
        }
        cart = this.unCommitCart(cart);
        items.remove(deletingItem);
        cart.setItems(items);
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
        //delete item
        itemServer.delete(deletingItem);
        LOGGER.debug("Item(id={}) removed from cart(id={})", itemId, cart.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public Cart unCommitCart(@NonNull Cart cart) throws BookNotFoundException, StripeException {
        if (cart.isCommitted()) {
            bookServer.restoreStock(cart.getItems());
            cart.setCommitted(false);
            cart.setCommittedAt(null);
            String piId = cart.getPiId();
            PaymentIntent.retrieve(piId).cancel();
            cart.setPiId(null);
            cart.setPiClientSecret(null);
            Cart savedCart = this.persist(cart);
            LOGGER.debug("Cart(id={}) successfully un-committed (pi id={})", cart.getId(), piId);
            return savedCart;
        }
        return cart;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = StripeException.class)
    public Cart commitCart(@NonNull User user) throws BookNotFoundException, ItemsAvailabilityException, StripeException {
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
            cart.setPiId(paymentIntent.getId());
            cart.setPiClientSecret(paymentIntent.getClientSecret());
            cart.setCommitted(true);
            cart.setCommittedAt(LocalDateTime.now());
            Cart savedCart = this.persist(cart);
            LOGGER.debug("User(id={}) cart(id={}) successfully committed (pi id={})", user.getId(), cart.getId(), cart.getPiId());
            return savedCart;
        }
        return cart;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void pruneCart(Cart cart, Set<Item> itemsDisabled) {
        if (itemsDisabled.isEmpty()) return;
        Set<Item> items = new HashSet<>(cart.getItems());
        itemsDisabled.forEach(item -> {
            items.remove(item);
            LOGGER.debug("Item(id={}) pruned from cart(id={}) due to book(id={}) is disabled", item.getId(), cart.getId(), item.getBook().getId());
        });
        cart.setItems(items);
        cartRepository.save(cart);
    }
}
