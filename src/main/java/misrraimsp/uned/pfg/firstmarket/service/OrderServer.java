package misrraimsp.uned.pfg.firstmarket.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.OrderStatus;
import misrraimsp.uned.pfg.firstmarket.converter.ConversionManager;
import misrraimsp.uned.pfg.firstmarket.data.OrderRepository;
import misrraimsp.uned.pfg.firstmarket.data.PaymentRepository;
import misrraimsp.uned.pfg.firstmarket.data.ShippingInfoRepository;
import misrraimsp.uned.pfg.firstmarket.event.OnCartCommittedEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentCancellationEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentSuccessEvent;
import misrraimsp.uned.pfg.firstmarket.exception.PaymentIntentProcessingTimeout;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${fm.payment.stripe.pi-minutes}")
    private final Long napMinutes = 30L;

    @Value("${fm.payment.stripe.limit-of-naps}")
    private final int limitOfNaps = 3;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ShippingInfoRepository shippingInfoRepository;
    private final AddressServer addressServer;
    private final CartServer cartServer;
    private final ConversionManager conversionManager;


    @Autowired
    public OrderServer(OrderRepository orderRepository,
                       PaymentRepository paymentRepository,
                       ShippingInfoRepository shippingInfoRepository,
                       AddressServer addressServer,
                       CartServer cartServer,
                       ConversionManager conversionManager) {

        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.shippingInfoRepository = shippingInfoRepository;
        this.addressServer = addressServer;
        this.cartServer = cartServer;
        this.conversionManager = conversionManager;
    }

    @Transactional
    @Async
    @EventListener
    public void handlePaymentSuccess(@NonNull OnPaymentSuccessEvent paymentSuccessEvent) throws StripeException {
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
        PaymentIntent paymentIntent = PaymentIntent.retrieve(cart.getStripePaymentIntentId());
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
        Address address = addressServer.persist(conversionManager.convertStripeAddressToAddress(stripeAddress));
        LOGGER.debug("User(id={}) address(id={}) successfully persisted", user.getId(), address.getId());
        //build shipping info
        ShippingInfo shippingInfo = conversionManager.convertStripeShippingDetailsToShippingInfo(shippingDetails);
        shippingInfo.setAddress(address);
        shippingInfo = shippingInfoRepository.save(shippingInfo);
        LOGGER.debug("User(id={}) shipping-info(id={}) successfully persisted", user.getId(), shippingInfo.getId());
        //build payment
        Payment payment = paymentRepository.save(conversionManager.convertStripePaymentIntentToPayment(paymentIntent));
        LOGGER.debug("User(id={}) payment(id={}) successfully persisted", user.getId(), payment.getId());
        //build order
        Order order = new Order();
        order.setUser(user);
        order.setItems(new HashSet<>(cart.getItems()));
        order.setShippingInfo(shippingInfo);
        order.setPayment(payment);
        order.setStatus(OrderStatus.PROCESSING);
        Order savedOrder = orderRepository.save(order);
        LOGGER.debug("User(id={}) order(id={}) successfully registered", user.getId(), savedOrder.getId());
        //update cart
        cart.setItems(new HashSet<>());
        cart.setCommitted(false);
        cart.setStripePaymentIntentId(null);
        cart.setStripeClientSecret(null);
        cartServer.persist(cart);
        LOGGER.debug("User(id={}) cart(id={}) successfully reset", user.getId(), cart.getId());
    }

    @Async
    @EventListener
    public void handlePaymentCancellation(@NonNull OnPaymentCancellationEvent paymentCancellationEvent) throws StripeException {
        cartServer.unCommitCart(paymentCancellationEvent.getUser().getCart());
    }

    public Page<Order> getOrdersByUser(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable);
    }

    @Async
    @EventListener
    public void handleCommitmentExpiration(@NonNull OnCartCommittedEvent cartCommittedEvent) throws PaymentIntentProcessingTimeout, StripeException {
        User user = cartCommittedEvent.getUser();
        assert user != null;
        assert user.getCart() != null;
        String paymentIntentId = user.getCart().getStripePaymentIntentId();
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
                    cartServer.unCommitCart(user.getCart());
                    piStatus = PaymentIntent.retrieve(paymentIntentId).getStatus();
            }
            LOGGER.debug("User(id={}) committed-cart(id={}) expiration handler finished (piId={}, piStatus={})", user.getId(), user.getCart().getId(), paymentIntentId, piStatus);
        }
        catch (InterruptedException e) {
            assert user.getCart().getId() != null;
            LOGGER.error("cart-committed event-handler error (userId={}, cartId={}): ", user.getId(), user.getCart().getId(), e);
        }
    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public void persistPayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
