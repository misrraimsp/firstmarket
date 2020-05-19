package misrraimsp.uned.pfg.firstmarket.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import misrraimsp.uned.pfg.firstmarket.event.OnCartCommittedEvent;
import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.BookOutOfStockException;
import misrraimsp.uned.pfg.firstmarket.exception.PaymentIntentProcessingTimeout;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class CheckoutServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${payment.stripe.pi-minutes}")
    private Long napMinutes = 30L;

    @Value("${payment.stripe.limit-of-naps}")
    private int limitOfNaps = 3;

    private BookServer bookServer;
    private CartServer cartServer;

    @Autowired
    public CheckoutServer(BookServer bookServer,
                          CartServer cartServer) {

        this.bookServer = bookServer;
        this.cartServer = cartServer;
    }

    @Transactional(rollbackFor = StripeException.class)
    public Cart commitCart(Cart cart) throws BookNotFoundException, BookOutOfStockException, StripeException {
        assert !cart.isCommitted();
        bookServer.removeFromStock(cart.getItems());
        cart.setCommitted(true);
        cart.setCommittedAt(LocalDateTime.now());
        PaymentIntent paymentIntent = PaymentIntent.create(PaymentIntentCreateParams
                .builder()
                .setCurrency("eur")
                .setAmount(cart.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .putMetadata("integration_check", "accept_a_payment")
                .build()
        );
        cart.setPiId(paymentIntent.getId());
        cart.setPiClientSecret(paymentIntent.getClientSecret());
        Cart committedCart = cartServer.persist(cart);
        LOGGER.debug("Cart(id={}) successfully committed (pi id={})", cart.getId(), cart.getPiId());
        return committedCart;
    }

    @Transactional(rollbackFor = StripeException.class)
    public Cart unCommitCart(Cart cart) throws BookNotFoundException, StripeException {
        assert cart.isCommitted();
        bookServer.restoreStock(cart.getItems());
        cart.setCommitted(false);
        cart.setCommittedAt(null);
        PaymentIntent.retrieve(cart.getPiId()).cancel();
        cart.setPiId(null);
        cart.setPiClientSecret(null);
        Cart uncommittedCart = cartServer.persist(cart);
        LOGGER.debug("Cart(id={}) successfully uncommitted", cart.getId());
        return uncommittedCart;
    }

    @Async
    @EventListener
    public void checkCommittedCartExpiration(@NotNull OnCartCommittedEvent cartCommittedEvent) throws PaymentIntentProcessingTimeout {
        Cart cart = cartCommittedEvent.getCommittedCart();
        assert cart != null;
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.retrieve(cart.getPiId());
            LOGGER.debug("cart-committed event STARTED (cartId={}, piId={}, piStatus={})", cart.getId(), paymentIntent.getId(), paymentIntent.getStatus());
            //take a nap
            TimeUnit.MINUTES.sleep(napMinutes);
            //wakeup
            int numOfNaps = 1;
            String piStatus = PaymentIntent.retrieve(cart.getPiId()).getStatus();
            while (piStatus.equals("processing") && numOfNaps < limitOfNaps) {
                //take another nap
                TimeUnit.MINUTES.sleep(napMinutes);
                //wakeup
                numOfNaps++;
                piStatus = PaymentIntent.retrieve(cart.getPiId()).getStatus();
            }
            if (piStatus.equals("processing")) {
                LOGGER.error("PaymentIntent with 'processing' status has timeout. (cartId={}, piId={})", cart.getId(), paymentIntent.getId());
                throw new PaymentIntentProcessingTimeout(paymentIntent.getId());
            }
            switch (piStatus) {
                case "canceled":
                case "succeeded":
                    break;
                default:
                    this.unCommitCart(cart);
                    piStatus = PaymentIntent.retrieve(cart.getPiId()).getStatus();
            }
            LOGGER.debug("cart-committed event ENDED (cartId={}, piId={}, piStatus={})", cart.getId(), paymentIntent.getId(), piStatus);
        }
        catch (InterruptedException e) {
            assert cart.getId() != null;
            LOGGER.error("cart-committed event error (cartId={}): ", cart.getId(), e);
        }
        catch (StripeException e) {
            LOGGER.warn("Stripe - Some exception occurred", e);
        }
    }

}
