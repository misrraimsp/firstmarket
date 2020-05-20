package misrraimsp.uned.pfg.firstmarket.controller;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import misrraimsp.uned.pfg.firstmarket.event.OnCartCommittedEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentCancellationEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentSuccessEvent;
import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.BookOutOfStockException;
import misrraimsp.uned.pfg.firstmarket.exception.UserNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OrderController extends BasicController {

    @Value("${payment.stripe.key.public}")
    private String spk = "somePublicKey_bitch";

    @Value("${payment.stripe.key.private}")
    private String ssk = "someSecretKey_bitch";

    private ApplicationEventPublisher applicationEventPublisher;

    public OrderController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer,
                           MessageSource messageSource,
                           OrderServer orderServer,
                           ApplicationEventPublisher applicationEventPublisher) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/user/orders")
    public String showOrders(Model model,
                             @AuthenticationPrincipal User authUser) {

        try {
            User user = userServer.findById(authUser.getId());
            model.addAttribute("orders", orderServer.getOrdersByUser(user));
            populateModel(model, authUser);
            return "orders";
        }
        catch (UserNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated user(id={}) does not exist'", authUser.getId(), e);
            return "redirect:/home";
        }
    }

    @GetMapping("/user/checkout")
    public String checkout(Model model,
                           @AuthenticationPrincipal User authUser) {

        Stripe.apiKey = ssk;

        try {
            User user = userServer.findById(authUser.getId());
            Cart cart = user.getCart();
            if (cart.isCommitted()) {
                LOGGER.debug("User(id={}) cart(id={}) is already committed (pi id={})", user.getId(), cart.getId(), cart.getPiId());
            }
            else {
                cart = orderServer.commitCart(user);
                applicationEventPublisher.publishEvent(new OnCartCommittedEvent(user));
                LOGGER.debug("cart-committed event published (userId={}, cartId={})", user.getId(), cart.getId());
            }
            model.addAttribute("cart", cart);
            populateModel(model, authUser);
            return "checkout";
        }
        catch (StripeException e) {
            LOGGER.warn("Stripe - Some exception occurred", e);
            return "redirect:/home";
        }
        catch (UserNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated user(id={}) does not exist'", authUser.getId(), e);
            return "redirect:/home";
        }
        catch (BookOutOfStockException e) {
            LOGGER.warn("Book run out of stock. Exception: ", e);
            return "redirect:/home";
        }
        catch (BookNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: registered book does not exist. Exception: ", e);
            return "redirect:/home";
        }
    }

    @PostMapping("/listener")
    public void processStripeEvent(@RequestBody String payload,
                                   @RequestHeader("Stripe-Signature") String sigHeader,
                                   HttpServletResponse response) {

        String eventType;
        PaymentIntent paymentIntent;

        // dev-localhost
        if (sigHeader.equals("local-dev")) {
            String[] parts = payload.split("-");
            eventType = parts[0];
            try {
                paymentIntent = PaymentIntent.retrieve(parts[1]);
            }
            catch (StripeException e) {
                LOGGER.warn("Stripe - Some exception occurred", e);
                response.setStatus(500);
                return;
            }
            //PaymentIntent paymentIntent = new Gson().fromJson(payload, PaymentIntent.class);
            // Deal with each scenario
            /*
            switch(payload) {
                case "succeeded":
                    // Fulfil the customer's purchase
                    LOGGER.debug("Stripe (localdev) - Succeeded: payment-intent");
                    response.setStatus(200);
                    return;
                case "payment_failed":
                    // Notify the customer that payment failed
                    LOGGER.debug("Stripe (localdev) - Failed: payment-intent");
                    response.setStatus(200);
                    return;
                default:
                    // Unexpected event type
                    LOGGER.debug("Stripe (localdev) - Unexpected event type");
                    response.setStatus(400);
            }
             */
        }
        // web deployed
        else {
            String endpointSecret = "anotherSecret_bitch";

            // Getting the event
            Event event;
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            }
            catch (JsonSyntaxException e) {
                // Invalid payload
                LOGGER.warn("Stripe - Invalid payload", e);
                response.setStatus(400);
                return;
            }
            catch (SignatureVerificationException e) {
                // Invalid signature
                LOGGER.warn("Stripe - Invalid signature", e);
                response.setStatus(400);
                return;
            }

            // Deserialize the nested object inside the event
            assert event != null;
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject;
            if (deserializer.getObject().isPresent()) {
                stripeObject = deserializer.getObject().get();
            }
            else {
                LOGGER.warn("Stripe - Event deserialization failed, probably due to an API version mismatch.");
                response.setStatus(200);
                return;
            }

            // Getting the payment-intent
            //PaymentIntent paymentIntent;
            if (stripeObject instanceof PaymentIntent) {
                paymentIntent = (PaymentIntent) stripeObject;
            }
            else {
                // for now other objects other than PaymentIntent are not accepted
                LOGGER.debug("Stripe - An object other than PaymentIntent has been sent to the listener");
                response.setStatus(200);
                return;
            }

            eventType = event.getType();
        }

        // Retrieve user
        String piUserId = paymentIntent.getMetadata().get("user-id");
        if (piUserId == null || piUserId.isBlank()) {
            LOGGER.error("Stripe - No user identification within PaymentIntent(id={}) sent to the stripe listener", paymentIntent.getId());
            return;
        }
        try {
            User user = userServer.findById(Long.parseLong(piUserId));
            switch(eventType) {
                case "payment_intent.succeeded":
                    LOGGER.debug("Stripe - user(id={}) PaymentIntent(id={}) SUCCEEDED", user.getId(), paymentIntent.getId());
                    applicationEventPublisher.publishEvent(new OnPaymentSuccessEvent(user));
                    LOGGER.debug("payment_intent.succeeded event published (userId={})", user.getId());
                    break;
                case "payment_intent.canceled":
                    LOGGER.debug("Stripe - user(id={}) PaymentIntent(id={}) CANCELED", user.getId(), paymentIntent.getId());
                    applicationEventPublisher.publishEvent(new OnPaymentCancellationEvent(user));
                    LOGGER.debug("payment_intent.succeeded event published (userId={})", user.getId());
                    break;
                case "payment_intent.payment_failed":
                    // Notify the customer that payment failed
                    LOGGER.debug("Stripe - user(id={}) PaymentIntent(id={}) FAILED", user.getId(), paymentIntent.getId());
                    break;
                case "payment_intent.created":
                case "payment_intent.processing":
                case "payment_intent.amount_capturable_updated":
                    // unhandled event type
                    LOGGER.debug("Stripe - UNHANDLED event (type={}) received related with user(id={}) PaymentIntent(id={})", eventType, user.getId(), paymentIntent.getId());
                    break;
                default:
                    // Unexpected event type
                    LOGGER.error("Stripe - UNEXPECTED event (type={}) received related with user(id={}) PaymentIntent(id={})", eventType, user.getId(), paymentIntent.getId());
            }
        }
        catch (UserNotFoundException e) {
            LOGGER.error("Stripe - No user(id={}) found with PaymentIntent(id={}) info sent to the stripe listener", piUserId, paymentIntent.getId());
        }
        catch (BookNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: registered book does not exist. Exception: ", e);
        }
        response.setStatus(200);
    }

}
