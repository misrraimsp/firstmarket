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
public class CheckoutController extends BasicController {

    @Value("${payment.stripe.key.public}")
    private String spk = "somePublicKey_bitch";

    @Value("${payment.stripe.key.private}")
    private String ssk = "someSecretKey_bitch";

    private CheckoutServer checkoutServer;
    private ApplicationEventPublisher applicationEventPublisher;

    public CheckoutController(UserServer userServer,
                              BookServer bookServer,
                              CatServer catServer,
                              ImageServer imageServer,
                              MessageSource messageSource,
                              PurchaseServer purchaseServer,
                              CheckoutServer checkoutServer,
                              ApplicationEventPublisher applicationEventPublisher) {

        super(userServer, bookServer, catServer, imageServer, messageSource, purchaseServer);
        this.checkoutServer = checkoutServer;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/user/checkout")
    public String checkout(Model model,
                           @AuthenticationPrincipal User authUser) {

        Stripe.apiKey = ssk;

        try {
            User user = userServer.findById(authUser.getId());
            Cart cart = user.getCart();
            if (cart.isCommitted()) {
                LOGGER.debug("Cart(id={}) is already committed (pi id={})", cart.getId(), cart.getPiId());
            }
            else {
                cart = checkoutServer.commitCart(cart);
                applicationEventPublisher.publishEvent(new OnCartCommittedEvent(cart));
                LOGGER.debug("cart-committed event published (cartId={})", cart.getId());
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

        // dev-localhost
        if (sigHeader.equals("localdev")) {
            //PaymentIntent paymentIntent = new Gson().fromJson(payload, PaymentIntent.class);
            // Deal with each scenario
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
                response.setStatus(400);
                return;
            }

            // Getting the payment-intent
            PaymentIntent paymentIntent;
            if (stripeObject instanceof PaymentIntent) {
                paymentIntent = (PaymentIntent) stripeObject;
            }
            else {
                // for now other objects other than PaymentIntent are not accepted
                LOGGER.debug("Stripe - An object other than PaymentIntent has been sent to the listener");
                response.setStatus(400);
                return;
            }

            // Deal with each scenario
            switch(event.getType()) {
                case "payment_intent.succeeded":
                    // Fulfil the customer's purchase
                    LOGGER.debug("Stripe - Succeeded: payment-intent id={}", paymentIntent.getId());
                    response.setStatus(200);
                    return;
                case "payment_intent.payment_failed":
                    // Notify the customer that payment failed
                    LOGGER.debug("Stripe - Failed: payment-intent id={}", paymentIntent.getId());
                    response.setStatus(200);
                    return;
                default:
                    // Unexpected event type
                    LOGGER.debug("Stripe - Unexpected event type");
                    response.setStatus(400);
            }
        }

    }

}
