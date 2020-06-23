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
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.PaymentProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.OrderStatus;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.OrderSortCriteria;
import misrraimsp.uned.pfg.firstmarket.event.OnCartCommittedEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentCancellationEvent;
import misrraimsp.uned.pfg.firstmarket.event.OnPaymentSuccessEvent;
import misrraimsp.uned.pfg.firstmarket.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.exception.ItemsAvailabilityException;
import misrraimsp.uned.pfg.firstmarket.model.Order;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController extends BasicController {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final CartServer cartServer;
    private final PaymentProperties paymentProperties;

    public OrderController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer,
                           MessageSource messageSource,
                           OrderServer orderServer,
                           ApplicationEventPublisher applicationEventPublisher,
                           CartServer cartServer,
                           PaymentProperties paymentProperties) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
        this.applicationEventPublisher = applicationEventPublisher;
        this.cartServer = cartServer;
        this.paymentProperties = paymentProperties;
    }

    @GetMapping("/orders")
    public String showOrders(@RequestParam(defaultValue = "${fm.pagination.default-index}") int pageNo,
                             @RequestParam(defaultValue = "${fm.pagination.default-size-index.order}") PageSize pageSize,
                             @RequestParam(defaultValue = "${fm.pagination.default-sort-index.order}") OrderSortCriteria sort,
                             Model model,
                             @AuthenticationPrincipal User authUser) {

        if (authUser == null) {
            LOGGER.warn("Anonymous user trying to access order info");
            return "redirect:/login";
        }
        populateModel(model.asMap(), authUser);
        populateModelToOrder(model, pageNo, pageSize, sort, authUser);
        return "orders";
    }

    private void populateModelToOrder(Model model,
                                      int pageNo,
                                      PageSize pageSize,
                                      OrderSortCriteria sort,
                                      User authUser) {

        Pageable pageable = PageRequest.of(pageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
        Page<Order> orderPage = (userServer.hasRole(authUser, "ROLE_ADMIN")) ? orderServer.findAll(pageable) : orderServer.getOrdersByUser(authUser, pageable);
        int lastPageNo = orderPage.getTotalPages() - 1;
        if (lastPageNo > 0 && lastPageNo < pageNo) {
            pageable = PageRequest.of(lastPageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
            orderPage = (userServer.hasRole(authUser, "ROLE_ADMIN")) ? orderServer.findAll(pageable) : orderServer.getOrdersByUser(authUser, pageable);
        }
        model.addAttribute("pageOfEntities", orderPage);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
    }

    @PostMapping("/admin/setOrderStatus")
    public ModelAndView processSetOrderStatus(ModelAndView modelAndView,
                                              @RequestParam Long orderId,
                                              @RequestParam OrderStatus orderStatus,
                                              @RequestParam(name = "pageNo") Optional<String> optPageNo,
                                              @RequestParam(name = "pageSize") Optional<String> optPageSize,
                                              @RequestParam(name = "sort") Optional<String> optSort) {

        orderServer.setStatus(orderId, orderStatus);
        modelAndView.setViewName("redirect:/orders");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> modelAndView.addObject("pageSize", pageSize));
        optSort.ifPresent(sort -> modelAndView.addObject("sort", sort));
        return modelAndView;
    }

    @GetMapping("/user/success")
    public String showSuccess(Model model,
                              @AuthenticationPrincipal User authUser) {

        populateModel(model.asMap(), authUser);
        populateModelToInfo(
                model.asMap(),
                "Success",
                messageSource.getMessage("success.title",null, null),
                List.of(messageSource.getMessage("success.message",null, null)),
                true);
        return "info";
    }

    @GetMapping("/user/checkout")
    public String checkout(Model model,
                           @AuthenticationPrincipal User authUser) throws StripeException {

        Stripe.apiKey = paymentProperties.getKey().get("private");
        User user = userServer.findById(authUser.getId());
        if (user.getCart().isCommitted()) {
            LOGGER.debug("User(id={}) cart(id={}) is already committed (pi id={})", user.getId(), user.getCart().getId(), user.getCart().getStripePaymentIntentId());
        }
        else {
            try {
                cartServer.commitCart(user);
                applicationEventPublisher.publishEvent(new OnCartCommittedEvent(user));
                LOGGER.debug("cart-committed event published (userId={}, cartId={})", user.getId(), user.getCart().getId());
            }
            catch (ItemsAvailabilityException e) {
                populateModel(model.asMap(), authUser);
                model.addAttribute("itemsDisabled", e.getItemsDisabled());
                model.addAttribute("itemsOutOfStock", e.getItemsOutOfStock());
                model.addAttribute("cartBookRegistry", bookServer.getCartBookRegistry());
                cartServer.pruneCart(user.getCart(), e.getItemsDisabled());
                e.getItemsOutOfStock().forEach(item ->
                        LOGGER.debug("Book(id={}) run out of stock (Item: id={}, quantity={})", item.getBook().getId(), item.getId(), item.getQuantity())
                );
                return "cart";
            }
        }
        populateModel(model.asMap(), authUser);
        return "checkoutForm";
    }

    @PostMapping("/listener")
    public void processStripeEvent(@RequestBody String payload,
                                   @RequestHeader("Stripe-Signature") String sigHeader,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        LOGGER.debug("POST at /listener from {}", request.getRemoteAddr());
        if (paymentProperties.getIps().stream().noneMatch(ip -> ip.equals(request.getRemoteAddr()))) {
            LOGGER.warn("Trying to POST at /listener from an unknown ip address {}", request.getRemoteAddr());
            response.setStatus(403);
            return;
        }

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
        }
        // web deployed
        else {
            // Getting the event
            Event event;
            try {
                event = Webhook.constructEvent(payload, sigHeader, paymentProperties.getKey().get("webhook"));
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
        catch (EntityNotFoundByIdException e) {
            if (e.getClassName().equals(User.class.getSimpleName())) {
                LOGGER.error("Stripe - No user(id={}) found with PaymentIntent(id={}) info sent to the stripe listener", piUserId, paymentIntent.getId());
            }
            throw e;
        }
        response.setStatus(200);
    }

}
