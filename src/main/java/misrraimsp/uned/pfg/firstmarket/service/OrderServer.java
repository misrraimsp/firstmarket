package misrraimsp.uned.pfg.firstmarket.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.data.OrderRepository;
import misrraimsp.uned.pfg.firstmarket.data.PaymentRepository;
import misrraimsp.uned.pfg.firstmarket.data.ShippingInfoRepository;
import misrraimsp.uned.pfg.firstmarket.event.OnCartCommittedEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentCancellationEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentSuccessEvent;
import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.BookOutOfStockException;
import misrraimsp.uned.pfg.firstmarket.exception.PaymentIntentProcessingTimeout;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${fm.payment.stripe.pi-minutes}")
    private Long napMinutes = 30L;

    @Value("${fm.payment.stripe.limit-of-naps}")
    private int limitOfNaps = 3;

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;
    private ShippingInfoRepository shippingInfoRepository;
    private AddressServer addressServer;
    private BookServer bookServer;
    private CartServer cartServer;
    private ConversionServer conversionServer;

    @Autowired
    public OrderServer(OrderRepository orderRepository,
                       PaymentRepository paymentRepository,
                       ShippingInfoRepository shippingInfoRepository,
                       AddressServer addressServer,
                       BookServer bookServer,
                       CartServer cartServer,
                       ConversionServer conversionServer) {

        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.shippingInfoRepository = shippingInfoRepository;
        this.addressServer = addressServer;
        this.bookServer = bookServer;
        this.cartServer = cartServer;
        this.conversionServer = conversionServer;
    }

    @Transactional
    @Async
    @EventListener
    public void handlePaymentSuccess(@NonNull OnPaymentSuccessEvent paymentSuccessEvent) {
        User user = paymentSuccessEvent.getUser();
        Cart cart = user.getCart();
        if (!cart.isCommitted()){
            LOGGER.error("User(id={}) trying to build a no-committed-cart(id={}) order", user.getId(), cart.getId());
            return;
        }
        if (cart.getItems().isEmpty()){
            LOGGER.error("User(id={}) trying to build a no-items-cart(id={}) order", user.getId(), cart.getId());
            return;
        }
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.retrieve(cart.getPiId());
        }
        catch (StripeException e) {
            LOGGER.error("Stripe - Some exception occurred", e);
            return;
        }
        ShippingDetails shippingDetails = paymentIntent.getShipping();
        if (shippingDetails == null){
            LOGGER.error("User(id={}) trying to build a no-shipping-details(piId={}) order", user.getId(), paymentIntent.getId());
            return;
        }
        com.stripe.model.Address stripeAddress = shippingDetails.getAddress();
        if (stripeAddress == null){
            LOGGER.error("User(id={}) trying to build a no-address(piId={}) order", user.getId(), paymentIntent.getId());
            return;
        }
        //build address
        Address address = addressServer.persist(conversionServer.convertStripeAddressToAddress(stripeAddress));
        LOGGER.debug("User(id={}) address(id={}) successfully persisted", user.getId(), address.getId());
        //build shipping info
        ShippingInfo shippingInfo = conversionServer.convertStripeShippingDetailsToShippingInfo(shippingDetails);
        shippingInfo.setAddress(address);
        shippingInfo = shippingInfoRepository.save(shippingInfo);
        LOGGER.debug("User(id={}) shipping-info(id={}) successfully persisted", user.getId(), shippingInfo.getId());
        //build payment
        Payment payment = paymentRepository.save(conversionServer.convertStripePaymentIntentToPayment(paymentIntent));
        LOGGER.debug("User(id={}) payment(id={}) successfully persisted", user.getId(), payment.getId());
        //build order
        Order order = new Order();
        order.setUser(user);
        order.setItems(new HashSet<>(cart.getItems()));
        order.setShippingInfo(shippingInfo);
        order.setPayment(payment);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        LOGGER.debug("User(id={}) order(id={}) successfully registered", user.getId(), savedOrder.getId());
        //update cart
        cart.setItems(new HashSet<>());
        cart.setCommitted(false);
        cart.setCommittedAt(null);
        cart.setPiId(null);
        cart.setPiClientSecret(null);
        cart.setLastModified(LocalDateTime.now());
        cartServer.persist(cart);
        LOGGER.debug("User(id={}) cart(id={}) successfully reset", user.getId(), cart.getId());
    }

    @Async
    @EventListener
    public void handlePaymentCancellation(@NonNull OnPaymentCancellationEvent paymentCancellationEvent) {
        try {
            this.unCommitCart(paymentCancellationEvent.getUser());
        }
        catch (StripeException e) {
            LOGGER.warn("Stripe - Some exception occurred", e);
        }
    }

    public Set<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Transactional(rollbackFor = StripeException.class)
    public void commitCart(@NonNull User user) throws BookNotFoundException, BookOutOfStockException, StripeException {
        assert user != null;
        Cart cart = user.getCart();
        assert !cart.isCommitted();
        bookServer.removeFromStock(cart.getItems());
        cart.setCommitted(true);
        cart.setCommittedAt(LocalDateTime.now());
        PaymentIntent paymentIntent = PaymentIntent.create(PaymentIntentCreateParams
                .builder()
                .setCurrency("eur")
                .setAmount(cart.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .putMetadata("integration_check", "accept_a_payment")
                .putMetadata("user-id", Long.toString(user.getId()))
                .build()
        );
        cart.setPiId(paymentIntent.getId());
        cart.setPiClientSecret(paymentIntent.getClientSecret());
        Cart committedCart = cartServer.persist(cart);
        LOGGER.debug("User(id={}) cart(id={}) successfully committed (pi id={})", user.getId(), cart.getId(), cart.getPiId());
    }

    @Transactional(rollbackFor = StripeException.class)
    public void unCommitCart(@NonNull User user) throws BookNotFoundException, StripeException {
        Cart cart = user.getCart();
        assert cart.isCommitted();
        bookServer.restoreStock(cart.getItems());
        cart.setCommitted(false);
        cart.setCommittedAt(null);
        String piId = cart.getPiId();
        PaymentIntent.retrieve(piId).cancel();
        cart.setPiId(null);
        cart.setPiClientSecret(null);
        cartServer.persist(cart);
        LOGGER.debug("User(id={}) cart(id={}) successfully un-committed (pi id={})", user.getId(), cart.getId(), piId);
    }

    @Async
    @EventListener
    public void handleCommitmentExpiration(@NonNull OnCartCommittedEvent cartCommittedEvent) throws PaymentIntentProcessingTimeout {
        User user = cartCommittedEvent.getUser();
        assert user != null;
        assert user.getCart() != null;
        String paymentIntentId = user.getCart().getPiId();
        try {
            LOGGER.debug("User(id={}) committed-cart(id={}) time-frame STARTED (piId={})", user.getId(), user.getCart().getId(), paymentIntentId);
            //take a nap
            TimeUnit.MINUTES.sleep(napMinutes);
            //wakeup
            LOGGER.debug("User(id={}) committed-cart(id={}) time-frame ENDED (piId={})", user.getId(), user.getCart().getId(), paymentIntentId);
            int numOfNaps = 1;
            String piStatus = PaymentIntent.retrieve(paymentIntentId).getStatus();
            while (piStatus.equals("processing") && numOfNaps < limitOfNaps) {
                LOGGER.debug("User(id={}) committed-cart(id={}) EXTENDED({} time/s) time-frame due to 'processing' piStatus STARTED (piId={})", user.getId(), user.getCart().getId(), numOfNaps, paymentIntentId);
                //take another nap
                TimeUnit.MINUTES.sleep(napMinutes);
                //wakeup
                LOGGER.debug("User(id={}) committed-cart(id={}) EXTENDED({} time/s) time-frame due to 'processing' piStatus ENDED (piId={})", user.getId(), user.getCart().getId(), numOfNaps, paymentIntentId);
                numOfNaps++;
                piStatus = PaymentIntent.retrieve(paymentIntentId).getStatus();
            }
            if (piStatus.equals("processing")) {
                LOGGER.error("User(id={}) PaymentIntent(id={}) with 'processing' status has TIMEOUT", user.getId(), paymentIntentId);
                throw new PaymentIntentProcessingTimeout(paymentIntentId);
            }
            switch (piStatus) {
                case "canceled":
                case "succeeded":
                    break;
                default:
                    this.unCommitCart(user);
                    piStatus = PaymentIntent.retrieve(paymentIntentId).getStatus();
            }
            LOGGER.debug("User(id={}) committed-cart(id={}) expiration handler finished (piId={}, piStatus={})", user.getId(), user.getCart().getId(), paymentIntentId, piStatus);
        }
        catch (InterruptedException e) {
            assert user.getCart().getId() != null;
            LOGGER.error("cart-committed event-handler error (userId={}, cartId={}): ", user.getId(), user.getCart().getId(), e);
        }
        catch (StripeException e) {
            LOGGER.warn("Stripe - Some exception occurred", e);
        }
    }

}